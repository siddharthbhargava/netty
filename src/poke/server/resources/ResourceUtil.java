/*
 * copyright 2012, gash
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
package poke.server.resources;

import java.util.Date;
import java.util.List;

import poke.server.conf.ServerConf;
import poke.util.UUIDGenerator;
import eye.Comm.Header;
import eye.Comm.Header.Routing;
import eye.Comm.Payload;
import eye.Comm.PhotoHeader;
import eye.Comm.PhotoHeader.RequestType;
import eye.Comm.PhotoHeader.ResponseFlag;
import eye.Comm.PhotoPayload;
import eye.Comm.PokeStatus;
import eye.Comm.Request;
import eye.Comm.RoutingPath;

public class ResourceUtil {

	/**
	 * Build a forwarding request message. Note this will return null if the
	 * server has already seen the request.
	 * 
	 * @param req
	 *            The request to forward
	 * @param cfg
	 *            The server's configuration
	 * @return The request with this server added to the routing path or null
	 */
	public static Request buildForwardMessage(Request req, ServerConf cfg) {

		List<RoutingPath> paths = req.getHeader().getPathList();
		if (paths != null) {
			// if this server has already seen this message return null
			for (RoutingPath rp : paths) {
				if (cfg.getNodeId() == rp.getNodeId())
					return null;
			}
		}

		Request.Builder bldr = Request.newBuilder(req);
		Header.Builder hbldr = bldr.getHeaderBuilder();
		RoutingPath.Builder rpb = RoutingPath.newBuilder();
		rpb.setNodeId(cfg.getNodeId());
		rpb.setTime(System.currentTimeMillis());
		hbldr.addPath(rpb.build());

		return bldr.build();
	}

	/**
	 * build the response header from a request
	 * 
	 * @param reqHeader
	 * @param status
	 * @param statusMsg
	 * @return
	 */
	public static Header buildHeaderFrom(Header reqHeader, PokeStatus status, String statusMsg) {
		return buildHeader(reqHeader.getRoutingId(), status, statusMsg, reqHeader.getOriginator(), reqHeader.getTag());
	}

	public static Header buildHeader(Routing path, PokeStatus status, String msg, int fromNode, String tag) {
		Header.Builder bldr = Header.newBuilder();
		bldr.setOriginator(fromNode);
		bldr.setRoutingId(path);
		bldr.setTag(tag);
		bldr.setReplyCode(status);

		if (msg != null)
			bldr.setReplyMsg(msg);

		bldr.setTime(System.currentTimeMillis());

		return bldr.build();
	}

	public static Request buildError(Header reqHeader, PokeStatus status, String statusMsg) {
		Request.Builder bldr = Request.newBuilder();
		Header hdr = buildHeaderFrom(reqHeader, status, statusMsg);
		bldr.setHeader(hdr);
		Payload.Builder pd= Payload.newBuilder();
		bldr.setBody(pd.build());

		// TODO add logging

		return bldr.build();
	}
	public static Header buildPhotoHeader(Request request, ResponseFlag rf, Date lastModified) {
		Header.Builder bldr = Header.newBuilder();
		bldr.setOriginator(ResourceFactory.getCfg().getNodeId());
		bldr.setRoutingId(request.getHeader().getRoutingId());
		PhotoHeader.Builder phb= PhotoHeader.newBuilder();
		phb.setRequestType(request.getHeader().getPhotoHeader().getRequestType());
		phb.setResponseFlag(rf);
		if(lastModified!=null)
			phb.setLastModified(lastModified.getTime());
		else
		{
			Date timeStamp = new Date();
			phb.setLastModified(timeStamp.getTime());
		}
		bldr.setPhotoHeader(phb);
		bldr.setTime(System.currentTimeMillis());

		return bldr.build();
	}

	public static Request modifyToIncludeUUID(Request req) {
		Request.Builder r= Request.newBuilder();
		r.setHeader(req.getHeader());
		PhotoPayload.Builder ppb= PhotoPayload.newBuilder();
        ppb.setUuid(UUIDGenerator.get().toString());
        //ppb.setData(null);
        ppb.setName(req.getBody().getPhotoPayload().getName());
        ppb.setData(req.getBody().getPhotoPayload().getData());
        
        
        //Setting Payload
        Payload.Builder pb=Payload.newBuilder();
        pb.setPhotoPayload(ppb.build());
        r.setBody(pb.build());
        
        Request reply = r.build();
		return reply;
	}
}
