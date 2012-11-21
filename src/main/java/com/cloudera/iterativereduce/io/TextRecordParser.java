package com.cloudera.iterativereduce.io;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;

import com.cloudera.iterativereduce.Updateable;

/**
 * A basic wrapper that uses hadoop's native classes for record reading
 * 
 * - read: this is a cheap hack to save time. but it works
 * 
 *
 * @param <T>
 */
public class TextRecordParser<T extends Updateable> implements RecordParser<T> {

  TextInputFormat input_format = null;
  InputSplit split = null;
  JobConf jobConf = null;
  
  RecordReader<LongWritable, Text> reader = null;
  LongWritable key = null;
  
  final Reporter voidReporter = Reporter.NULL;
  boolean hasMore = true;
  
  /*
   * a hack to get this setup
   */
/*  public void setup(JobConf jobConf, InputSplit split) throws IOException {
    
    this.jobConf = jobConf;
    this.split = split;
    this.input_format = new TextInputFormat();

    this.reader = input_format.getRecordReader(this.split, this.jobConf, voidReporter);
    this.key = reader.createKey();
    
    
  }
*/  
  /**
   * 
   * just a dead simple way to do this
   *
   * - functionality from TestTextInputFormat::readSplit()
   * 
   * If returns true, then csv_line contains the next line
   * If returns false, then there is no next record
   * 
   * Will terminate when it hits the end of the split based on the information provided in the split class
   * to the constructor and the TextInputFormat
   * 
   * @param csv_line
   * @throws IOException 
   */
  public boolean next(Text csv_line) throws IOException {
    
    hasMore = reader.next(key, csv_line);
    return hasMore;
    
  }
  

  
  @Override
  public int getCurrentRecordsProcessed() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean hasMoreRecords() {
    // TODO Auto-generated method stub
    return hasMore;
  }

  @Override
  public T nextRecord() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * use this to setup the input format
   */
  @Override
  public void parse() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void reset() {
    // TODO Auto-generated method stub
    try {
      this.hasMore = true;
      this.reader = input_format.getRecordReader(this.split, this.jobConf, voidReporter);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void setFile(String file, long offset, long length) {
    JobConf defaultConf = new JobConf();
    this.split = new FileSplit( new Path( file ), offset, length, defaultConf); 

    this.hasMore = true;
    this.jobConf = defaultConf;
    //this.split = split;
    this.input_format = new TextInputFormat();

    try {
      this.reader = input_format.getRecordReader(this.split, this.jobConf, voidReporter);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    this.key = reader.createKey();
    
  }

  @Override
  public void setFile(String file) {
    // TODO Auto-generated method stub
    
  }

}
