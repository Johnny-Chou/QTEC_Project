package com.im.qtec.utils;

import com.blankj.utilcode.util.EncryptUtils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * Created by zhouyanglei on 2017/12/19.
 */

public class MessageUtils {
    static {
        EncryptUtils.AES_Transformation = "AES/ECB/PKCS7Padding";
        Security.addProvider(new BouncyCastleProvider());
    }

    public static int getTime(byte[] message) {
        return bytesToInt(message, 28);
    }

    public static char getHeaderLength(byte[] message) {
        return bytesToChar(message, 0);
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    public static byte[] charToBytes(char value) {
        byte[] src = new byte[2];
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    public static byte[] getMessageContent(byte[] message) {
        byte[] content = new byte[message.length - getHeaderLength(message)];
        for (int i = 0; i < content.length; i++) {
            content[i] = message[getHeaderLength(message) + i];
        }
        return content;
    }

    public static byte[] getMessageKey(byte[] message) {
        byte[] key = new byte[16];
        for (int i = 0; i < key.length; i++) {
            key[i] = message[2 + i];
        }
        return key;
    }

    public static int getSenderid(byte[] message) {
        byte[] senderid = new byte[4];
        for (int i = 0; i < senderid.length; i++) {
            senderid[i] = message[18 + i];
        }
        return bytesToInt(senderid, 0);
    }


    public static int getReceiverid(byte[] message) {
        byte[] receiverid = new byte[4];
        for (int i = 0; i < receiverid.length; i++) {
            receiverid[i] = message[22 + i];
        }
        return bytesToInt(receiverid, 0);
    }

    public static byte getMessageType(byte[] message) {
        return message[26];
    }


    public static byte getVoiceLength(byte[] message) {
        return message[32];
    }

    public static boolean isDelete(byte[] message) {
        return message[27] == 0 ? false : true;
    }

    /*public static int getTimestamp(byte[] message) {
        byte[] timestamp = new byte[4];
        for (int i = 0; i < timestamp.length; i++) {
            timestamp[i] = message[28 + i];
        }
        return bytesToInt(timestamp, 0);
    }*/

    public static String getMessage(byte[] message) {
        byte[] plain = new byte[]{0};
        try {
            plain = EncryptUtils.decryptAES(getMessageContent(message), getMessageKey(message));
            //plain = getMessageContent(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(plain);
    }

    public static byte[] getEncryptedMessage(char headerLength, byte[] key, int senderid, int receiverid, byte messageType, byte isDelete, int timestamp, byte[] messageContent) {
        byte[] header = charToBytes(headerLength);
        byte[] sender = intToBytes(senderid);
        byte[] receiver = intToBytes(receiverid);
        byte[] time = intToBytes(timestamp);
        try {
            byte[] encryptContent = EncryptUtils.encryptAES(messageContent, key);
            byte[] message = new byte[headerLength + encryptContent.length];
            for (int i = 0; i < message.length; i++) {
                if (i < 2) {
                    message[i] = header[i];
                } else if (i < 18) {
                    message[i] = key[i - 2];
                } else if (i < 22) {
                    message[i] = sender[i - 18];
                } else if (i < 26) {
                    message[i] = receiver[i - 22];
                } else if (i < 27) {
                    message[i] = messageType;
                } else if (i < 28) {
                    message[i] = isDelete;
                } else if (i < 32) {
                    message[i] = time[i - 28];
                }else if (i >= headerLength) {
                    message[i] = encryptContent[i - headerLength];
                }
            }
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] setMessageContent(byte[] message,  byte[] messageContent) {
        char headerLength = getHeaderLength(message);
        byte[] newMessage = new byte[headerLength + messageContent.length];
        try {
            for (int i = 0; i < headerLength; i++) {
                newMessage[i] = message[i];
            }
            for (int i = headerLength; i < newMessage.length; i++) {
                newMessage[i] = messageContent[i - headerLength];
            }
            return newMessage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getEncryptedMessage(char headerLength, byte[] key, int senderid, int receiverid, byte messageType, byte isDelete, int timestamp,int voiceLength, byte[] messageContent) {
        byte[] header = charToBytes(headerLength);
        byte[] sender = intToBytes(senderid);
        byte[] receiver = intToBytes(receiverid);
        byte[] time = intToBytes(timestamp);
        try {
            byte[] encryptContent = EncryptUtils.encryptAES(messageContent, key);
            byte[] message = new byte[headerLength + encryptContent.length];
            for (int i = 0; i < message.length; i++) {
                if (i < 2) {
                    message[i] = header[i];
                } else if (i < 18) {
                    message[i] = key[i - 2];
                } else if (i < 22) {
                    message[i] = sender[i - 18];
                } else if (i < 26) {
                    message[i] = receiver[i - 22];
                } else if (i < 27) {
                    message[i] = messageType;
                } else if (i < 28) {
                    message[i] = isDelete;
                } else if (i < 32) {
                    message[i] = time[i - 28];
                } else if (messageType == 3 && i == 32) {
                    message[i] = (byte) voiceLength;
                } else if (i >= headerLength) {
                    message[i] = encryptContent[i - headerLength];
                }
            }
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


    public static char bytesToChar(byte[] src, int offset) {
        char value;
        value = (char) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8));
        return value;
    }

    public static String getPath(byte[] message) {
        char headerLength = getHeaderLength(message);
        byte[] newMessage = new byte[message.length - headerLength];
        for (int i = headerLength; i < message.length; i++) {
            newMessage[i - headerLength] = message[i];
        }
        return new String(newMessage);
    }
}
