/*
 * copyright 2014, gash
 * 
 * Gash licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package poke.server.queue;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.lang.Thread.State;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import poke.client.ClientCommand;
import poke.client.ClientPrintListener;
import poke.client.ResponseListener;
import poke.client.comm.CommHandler;
import poke.server.conf.NodeDesc;
import poke.server.managers.ClientCommandManager;
import poke.server.resources.Resource;
import poke.server.resources.ResourceFactory;
import poke.server.resources.ResourceUtil;

import com.google.protobuf.GeneratedMessage;

import eye.Comm.PokeStatus;
import eye.Comm.Request;
import eye.Comm.PhotoHeader.RequestType;

import com.google.protobuf.UninitializedMessageException;

/**
 * A server queue exists for each connection (channel). A per-channel queue
 * isolates clients. However, with a per-client model. The server is required to
 * use a master scheduler/coordinator to span all queues to enact a QoS policy.
 * 
 * How well does the per-channel work when we think about a case where 1000+
 * connections?
 * 
 * @author gash
 * 
 */
public class PerChannelQueue implements ChannelQueue {
	protected static Logger logger = LoggerFactory.getLogger("server");

	private Channel channel;

	// The queues feed work to the inbound and outbound threads (workers). The
	// threads perform a blocking 'get' on the queue until a new event/task is
	// enqueued. This design prevents a wasteful 'spin-lock' design for the
	// threads
	private LinkedBlockingDeque<com.google.protobuf.GeneratedMessage> inbound;
	private LinkedBlockingDeque<com.google.protobuf.GeneratedMessage> outbound;

	// This implementation uses a fixed number of threads per channel
	private OutboundWorker oworker;
	private InboundWorker iworker;

	// not the best method to ensure uniqueness
	private ThreadGroup tgroup = new ThreadGroup("ServerQueue-" + System.nanoTime());

	protected PerChannelQueue(Channel channel) {
		this.channel = channel;
		init();
	}

	protected void init() {
		inbound = new LinkedBlockingDeque<com.google.protobuf.GeneratedMessage>();
		outbound = new LinkedBlockingDeque<com.google.protobuf.GeneratedMessage>();
		for(int i=0;i<10;i++)
		{
		iworker = new InboundWorker(tgroup, 1, this);
		iworker.start();
		oworker = new OutboundWorker(tgroup, 1, this);
		oworker.start();
		}

		// let the handler manage the queue's shutdown
		// register listener to receive closing of channel
		// channel.getCloseFuture().addListener(new CloseListener(this));
	}

	protected Channel getChannel() {
		return channel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see poke.server.ChannelQueue#shutdown(boolean)
	 */
	@Override
	public void shutdown(boolean hard) {
		logger.info("server is shutting down");
		channel = null;

		if (hard) {
			// drain queues, don't allow graceful completion
			inbound.clear();
			outbound.clear();
		}

		if (iworker != null) {
			iworker.forever = false;
			if (iworker.getState() == State.BLOCKED || iworker.getState() == State.WAITING)
				iworker.interrupt();
			iworker = null;
		}

		if (oworker != null) {
			oworker.forever = false;
			if (oworker.getState() == State.BLOCKED || oworker.getState() == State.WAITING)
				oworker.interrupt();
			oworker = null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see poke.server.ChannelQueue#enqueueRequest(eye.Comm.Finger)
	 */
	@Override
	public void enqueueRequest(Request req, Channel notused) {
		try {
			inbound.put(req);
		} catch (InterruptedException e) {
			logger.error("message not enqueued for processing", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see poke.server.ChannelQueue#enqueueResponse(eye.Comm.Response)
	 */
	@Override
	public void enqueueResponse(Request reply, Channel notused) {
		if (reply == null)
			return;

		try {
			logger.info("reply:" + reply);
			outbound.put(reply);
		} catch (InterruptedException e) {
			logger.error("message not enqueued for reply", e);
		}
	}

	protected class OutboundWorker extends Thread {
		int workerId;
		PerChannelQueue sq;
		boolean forever = true;

		public OutboundWorker(ThreadGroup tgrp, int workerId, PerChannelQueue sq) {
			super(tgrp, "outbound-" + workerId);
			this.workerId = workerId;
			this.sq = sq;

			if (outbound == null)
				throw new RuntimeException("connection worker detected null queue");
		}

		@Override
		public void run() {
			Channel conn = sq.channel;
			if (conn == null || !conn.isOpen()) {
				PerChannelQueue.logger.error("connection missing, no outbound communication");
				return;
			}

			while (true) {
				//PerChannelQueue.logger.info("Forever:" + forever + "outbound queue size: "+ sq.outbound.size());
				if (!forever && sq.outbound.size() == 0)
					break;

				try {
					// block until a message is enqueued
					GeneratedMessage msg = sq.outbound.take();
					final GeneratedMessage cfMsg = msg; //to put for channel future failure
					if (conn.isWritable()) {
						boolean rtn = false;
						if (channel != null && channel.isOpen() && channel.isWritable()) {
							ChannelFuture cf = channel.writeAndFlush(msg);

							//to provide non-blocking functionality for channel write
							cf.addListener(new ChannelFutureListener(){
								public void operationComplete(ChannelFuture future) throws Exception {
									//check if operation was successfull
									if(future.isSuccess()) {
										logger.info("Written to channel successfully");
									} else {
										sq.outbound.putFirst(cfMsg);
									}
								}
							});

							/*
							// blocks on write - use listener to be async
							cf.awaitUninterruptibly();
							rtn = cf.isSuccess();
							if (!rtn)
								sq.outbound.putFirst(msg);
							 */
						}

					} else
						sq.outbound.putFirst(msg);
				} catch (InterruptedException ie) {
					break;
				} catch (Exception e) {
					PerChannelQueue.logger.error("Unexpected communcation failure", e);
					break;
				}
			}

			if (!forever) {
				PerChannelQueue.logger.info("connection queue closing");
			}
		}
	}

	protected class InboundWorker extends Thread {
		int workerId;
		PerChannelQueue sq;
		boolean forever = true;

		public InboundWorker(ThreadGroup tgrp, int workerId, PerChannelQueue sq) {
			super(tgrp, "inbound-" + workerId);
			this.workerId = workerId;
			this.sq = sq;

			if (outbound == null)
				throw new RuntimeException("connection worker detected null queue");
		}

		@Override
		public void run() {
			Channel conn = sq.channel;
			if (conn == null || !conn.isOpen()) {
				PerChannelQueue.logger.error("connection missing, no inbound communication");
				return;
			}

			while (true) {
				if (!forever && sq.inbound.size() == 0)
					break;

				try {
					// block until a message is enqueued
					GeneratedMessage msg = sq.inbound.take();

					// process request and enqueue response
					if (msg instanceof Request) {
						Request req = ((Request) msg);
						if(req.getHeader().getPhotoHeader().getRequestType()==RequestType.write)
						{ //generate and append uuid to the request
							req=ResourceUtil.modifyToIncludeUUID(req);
						}
						// do we need to route the request?
						boolean needToRoute= ResourceFactory.getInstance().needToRoute(req.getHeader(), req.getBody().getPhotoPayload().getUuid());
						Request reply = null;
						if(!needToRoute)
						{
							// handle it locally
							Resource rsc = ResourceFactory.getInstance().resourceInstance(req.getHeader());


							if (rsc == null) {
								logger.error("failed to obtain resource for " + req);
								reply = ResourceUtil.buildError(req.getHeader(), PokeStatus.NORESOURCE,
										"Request not processed");
							} else
								reply = rsc.process(req);
						}
						else
						{
							NodeDesc destinationNode = ResourceFactory.getInstance().getDeterminedNode(req.getHeader(), req.getBody().getPhotoPayload().getUuid());
							ClientCommand destHop=null;
							if(ClientCommandManager.hasConnection(destinationNode.getNodeId()))
								destHop=ClientCommandManager.getConnection(destinationNode.getNodeId());
							else
							{
								destHop=new ClientCommand(destinationNode.getHost(), destinationNode.getPort());
								ClientCommandManager.addConnection(destinationNode.getNodeId(), destHop);
							}
							ResponseListener rl=new ResponseListener("response");
							destHop.forwardJob(req,destinationNode.getNodeId());
							destHop.addListener(rl);
							while(true)
							{
								if(CommHandler.getResponse()!=null)
								{
									reply=CommHandler.getResponse();
									break;
								}
							}
							
						}
						System.out.println("");
						sq.enqueueResponse(reply, null);
						
						
						//replication Logic
//						if(req.getHeader().getPhotoHeader().getRequestType()==RequestType.write || req.getHeader().getPhotoHeader().getRequestType()==RequestType.delete)
//						{
//							List<NodeDesc> replicas= ResourceFactory.getInstance().getReplicationNodes(req.getHeader(), req.getBody().getPhotoPayload().getUuid());
//							for(NodeDesc replica : replicas)
//							{
//								ClientCommand replicaHop=new ClientCommand(replica.getHost(), replica.getPort());
//								replicaHop.forwardJob(req,replica.getNodeId());
//							}
//						}
					}

				} catch (InterruptedException ie) {
					break;
				} /*catch (UninitializedMessageException ume){
					System.out.println("Kuch bhi ayega toh pata chalega");
					logger.info("Fields details: " + ume.getMissingFields());
					break;
				} */catch (Exception e) {
					PerChannelQueue.logger.error("Unexpected processing failure", e);
					break;
				}
			}

			if (!forever) {
				PerChannelQueue.logger.info("connection queue closing");
			}
		}
	}

	public class CloseListener implements ChannelFutureListener {
		private ChannelQueue sq;

		public CloseListener(ChannelQueue sq) {
			this.sq = sq;
		}

		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			sq.shutdown(true);
		}
	}
}
