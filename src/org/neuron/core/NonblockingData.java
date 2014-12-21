package org.neuron.core;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;



public class NonblockingData {
	
	public class DataQueue{
		private Queue<ByteBuffer> datas=new LinkedList<>();
		
		public void appends(ByteBuffer[] data){
			for(ByteBuffer bf:data){
				datas.add(bf);
			}
		}
		
		public void append(ByteBuffer data){
			datas.add(data);
		}
		
		public byte[] convertToBytes(){
			int size=0;
			byte[][] bs=new byte[datas.size()][];
			int index=0;
			for(ByteBuffer bf:datas){
				int ts=bf.remaining();
				size+=ts;
				bs[index]=new byte[ts];
				bf.get(bs[index++],0,ts);
			}
			byte[] result=new byte[size];
			int offset=0;
			for(byte[] b:bs){
				if(b!=null){
					System.arraycopy(b, 0, result, offset, b.length);
					offset+=b.length;
				}
			}
			return result;
		}
	}

	private DataQueue readQueue;
	private DataQueue writeQueue;
	
	public NonblockingData(){
		readQueue=new DataQueue();
		writeQueue=new DataQueue();
	}
	
	public void appendDataToReadQueue(ByteBuffer data){
		readQueue.append(data);
	}
	
	public void appendDataToWriteQueue(ByteBuffer data){
		writeQueue.append(data);
	}
	
	public byte[] getReadDataBytes(){
		return readQueue.convertToBytes();
	}
}
