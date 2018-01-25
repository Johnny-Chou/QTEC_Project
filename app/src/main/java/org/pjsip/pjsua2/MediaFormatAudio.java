/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.pjsip.pjsua2;

public class MediaFormatAudio extends MediaFormat {
  private transient long swigCPtr;

  protected MediaFormatAudio(long cPtr, boolean cMemoryOwn) {
    super(pjsua2JNI.MediaFormatAudio_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(MediaFormatAudio obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        pjsua2JNI.delete_MediaFormatAudio(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public void setClockRate(long value) {
    pjsua2JNI.MediaFormatAudio_clockRate_set(swigCPtr, this, value);
  }

  public long getClockRate() {
    return pjsua2JNI.MediaFormatAudio_clockRate_get(swigCPtr, this);
  }

  public void setChannelCount(long value) {
    pjsua2JNI.MediaFormatAudio_channelCount_set(swigCPtr, this, value);
  }

  public long getChannelCount() {
    return pjsua2JNI.MediaFormatAudio_channelCount_get(swigCPtr, this);
  }

  public void setFrameTimeUsec(long value) {
    pjsua2JNI.MediaFormatAudio_frameTimeUsec_set(swigCPtr, this, value);
  }

  public long getFrameTimeUsec() {
    return pjsua2JNI.MediaFormatAudio_frameTimeUsec_get(swigCPtr, this);
  }

  public void setBitsPerSample(long value) {
    pjsua2JNI.MediaFormatAudio_bitsPerSample_set(swigCPtr, this, value);
  }

  public long getBitsPerSample() {
    return pjsua2JNI.MediaFormatAudio_bitsPerSample_get(swigCPtr, this);
  }

  public void setAvgBps(long value) {
    pjsua2JNI.MediaFormatAudio_avgBps_set(swigCPtr, this, value);
  }

  public long getAvgBps() {
    return pjsua2JNI.MediaFormatAudio_avgBps_get(swigCPtr, this);
  }

  public void setMaxBps(long value) {
    pjsua2JNI.MediaFormatAudio_maxBps_set(swigCPtr, this, value);
  }

  public long getMaxBps() {
    return pjsua2JNI.MediaFormatAudio_maxBps_get(swigCPtr, this);
  }

  public MediaFormatAudio() {
    this(pjsua2JNI.new_MediaFormatAudio(), true);
  }

}
