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
package poke.resources;

import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import com.mongodb.*;
import com.mongodb.gridfs.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import poke.server.resources.Resource;
import poke.server.resources.ResourceUtil;
import eye.Comm.PhotoPayload;
import eye.Comm.Payload;
import eye.Comm.JobOperation;
import eye.Comm.Ping;
import eye.Comm.PokeStatus;
import eye.Comm.Request;

public class JobResource implements Resource {
	protected static Logger logger = LoggerFactory.getLogger("server");
	@Override
	public Request process(Request request) {
		// TODO add code to process the message/event received
		//logger.info("poke: " + request.getBody().getPhotoPayload().data);
		
		com.google.protobuf.ByteString a = request.getBody().getPhotoPayload().getData();
		byte[] b = a.toByteArray();
		String s = a.toString();
		
		try
	     {
		Mongo mongo = new Mongo("127.0.0.1", 27017);
        String dbName = "ImageDB";
        DB db = mongo.getDB( dbName );
        //Create GridFS object
        GridFS fs = new GridFS( db );
        //Save image into database
        GridFSInputFile in = fs.createFile( b );
        in.setFilename(request.getBody().getPhotoPayload().getName());
        in.save();
        System.out.println("Image saved in DB");
        //Find saved image
        GridFSDBFile out = fs.findOne( new BasicDBObject( "_id" , in.getId() ) );
	    
		
        //Save loaded image from database into new image file
      //  FileOutputStream outputImage = new FileOutputStream("/home/jaymit/Documents/"+request.getBody().getPhotoPayload().getName());
      //  out.writeTo( outputImage );
        //outputImage.close();
		
		/*String strFilePath = "/home/jaymit/"+request.getBody().getPhotoPayload().getName();
		
		 try
	     {*/
	      FileOutputStream fos = new FileOutputStream("/home/jaymit/Documents/"+request.getBody().getPhotoPayload().getName());
	    
	     
	       out.writeTo(fos);
	   
	     
	       fos.close();
	     
	     }
	     catch(FileNotFoundException ex)
	     {
	      System.out.println("FileNotFoundException : " + ex);
	     }
		 catch(IOException ioe)
	     {
	      System.out.println("IOException : " + ioe);
	     }
		Request.Builder rb = Request.newBuilder();

		// metadata
		rb.setHeader(ResourceUtil.buildPhotoHeader(request));
		
		// payload
		PhotoPayload.Builder ppb= PhotoPayload.newBuilder();
		//generate uuid
		ppb.setUuid("Test1");
		ppb.setName(request.getBody().getPhotoPayload().getName());
		//ppb.setData("");
		Payload.Builder pb=Payload.newBuilder();
		pb.setPhotoPayload(ppb.build());
		
		rb.setBody(pb.build());

		Request reply = rb.build();

		return reply;
	}

}
