package com.im.qtec.utils;

import com.blankj.utilcode.util.EncryptUtils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import cn.qtec.qkcl.crypto.enums.EEncrypAlg;
import cn.qtec.qkcl.envelope.EnvelopEnc;
import cn.qtec.qkcl.envelope.EnvelopEncInterface;
import cn.qtec.qkcl.envelope.pojo.QtKey;
import cn.qtec.qkcl.envelope.pojo.QtKeyId;
import cn.qtec.qkcl.envelope.pojo.QtRecipientInfo;
import cn.qtec.qkcl.envelope.pojo.ResultInfo;

/**
 * Created by zhouyanglei on 2017/12/19.
 */

public class MessageUtils {
//    static {
//        EncryptUtils.AES_Transformation = "AES/ECB/PKCS7Padding";
//        Security.addProvider(new BouncyCastleProvider());
//    }

    public static int getTime(byte[] message) {
        return bytesToInt(message, 48);
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

    public static byte[] getMessageKeyId(byte[] message) {
        byte[] key = new byte[36];
        for (int i = 0; i < key.length; i++) {
            key[i] = message[2 + i];
        }
        return key;
    }

    public static int getSenderid(byte[] message) {
        byte[] senderid = new byte[4];
        for (int i = 0; i < senderid.length; i++) {
            senderid[i] = message[38 + i];
        }
        return bytesToInt(senderid, 0);
    }


    public static int getReceiverid(byte[] message) {
        byte[] receiverid = new byte[4];
        for (int i = 0; i < receiverid.length; i++) {
            receiverid[i] = message[42 + i];
        }
        return bytesToInt(receiverid, 0);
    }

    public static byte getMessageType(byte[] message) {
        return message[46];
    }


    public static byte getVoiceLength(byte[] message) {
        return message[52];
    }

    public static boolean isDelete(byte[] message) {
        return message[47] == 0 ? false : true;
    }

    /*public static int getTimestamp(byte[] message) {
        byte[] timestamp = new byte[4];
        for (int i = 0; i < timestamp.length; i++) {
            timestamp[i] = message[28 + i];
        }
        return bytesToInt(timestamp, 0);
    }*/

    /*public static String getMessage(char headerLength, String keyId, int senderid, int receiverid, byte messageType, byte isDelete, int timestamp, byte[] messageContent) {
        byte[] header = charToBytes(headerLength);
        byte[] sender = intToBytes(senderid);
        byte[] receiver = intToBytes(receiverid);
        byte[] time = intToBytes(timestamp);

        EnvelopEncInterface envelopEnc = EnvelopEnc.getInstance();
        List<QtRecipientInfo> list = new ArrayList();
        QtRecipientInfo qtRecipientInfo = new QtRecipientInfo();
        qtRecipientInfo.seteEncrypAlg(EEncrypAlg.AES256_CBC_PKCS5PADDING);
        QtKey qtKey = new QtKey();
        for (int i = 0; i < qtKey.getBuffer().length; i++) {
            qtKey.getBuffer()[i] = key[i];
        }
        qtRecipientInfo.setKey(qtKey);
        QtKeyId qtKeyId = new QtKeyId();
        for (int i = 0; i < qtKeyId.getBuffer().length; i++) {
            qtKeyId.getBuffer()[i] = keyId.getBytes()[i];
        }
        qtRecipientInfo.setKeyId(qtKeyId);
        list.add(qtRecipientInfo);

        try {
            byte[] result = envelopEnc.encryptByteArray(messageContent, list, EEncrypAlg.AES256_CBC_PKCS5PADDING, "TestName");
            // ResultInfo resultInfo=envelopEnc.decryptByteArray(result,qtKeyId,qtKey);
//            System.out.println(resultInfo.getName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {

            byte[] message = new byte[headerLength + result.length];
            for (int i = 0; i < message.length; i++) {
                if (i < 2) {
                    message[i] = header[i];
                } else if (i < 38) {
                    message[i] = keyId.getBytes()[i - 2];
                } else if (i < 42) {
                    message[i] = sender[i - 38];
                } else if (i < 46) {
                    message[i] = receiver[i - 42];
                } else if (i < 47) {
                    message[i] = messageType;
                } else if (i < 48) {
                    message[i] = isDelete;
                } else if (i < 52) {
                    message[i] = time[i - 48];
                } else if (i >= headerLength) {
                    message[i] = result[i - headerLength];
                }
            }
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }*/

    public static byte[] getEncryptMessage(char headerLength, String keyId, byte[] key, int senderid, int receiverid, byte messageType, byte isDelete, int timestamp, byte[] messageContent) {
        byte[] header = charToBytes(headerLength);
        byte[] sender = intToBytes(senderid);
        byte[] receiver = intToBytes(receiverid);
        byte[] time = intToBytes(timestamp);

        EnvelopEncInterface envelopEnc = EnvelopEnc.getInstance();
        List<QtRecipientInfo> list = new ArrayList();
        QtRecipientInfo qtRecipientInfo = new QtRecipientInfo();
        qtRecipientInfo.seteEncrypAlg(EEncrypAlg.AES256_CBC_PKCS5PADDING);
        QtKey qtKey = new QtKey();
        for (int i = 0; i < qtKey.getBuffer().length; i++) {
            qtKey.getBuffer()[i] = key[i];
        }
        qtRecipientInfo.setKey(qtKey);
        QtKeyId qtKeyId = new QtKeyId();
        for (int i = 0; i < qtKeyId.getBuffer().length; i++) {
            qtKeyId.getBuffer()[i] = keyId.getBytes()[i];
        }
        qtRecipientInfo.setKeyId(qtKeyId);
        list.add(qtRecipientInfo);

        try {
            byte[] result = envelopEnc.encryptByteArray(messageContent, list, EEncrypAlg.AES256_CBC_PKCS5PADDING, "TestName");
            // ResultInfo resultInfo=envelopEnc.decryptByteArray(result,qtKeyId,qtKey);
//            System.out.println(resultInfo.getName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {

            byte[] message = new byte[headerLength + result.length];
            for (int i = 0; i < message.length; i++) {
                if (i < 2) {
                    message[i] = header[i];
                } else if (i < 38) {
                    message[i] = keyId.getBytes()[i - 2];
                } else if (i < 42) {
                    message[i] = sender[i - 38];
                } else if (i < 46) {
                    message[i] = receiver[i - 42];
                } else if (i < 47) {
                    message[i] = messageType;
                } else if (i < 48) {
                    message[i] = isDelete;
                } else if (i < 52) {
                    message[i] = time[i - 48];
                } else if (i >= headerLength) {
                    message[i] = result[i - headerLength];
                }
            }
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] setMessageContent(byte[] message, byte[] messageContent) {
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

    public static byte[] getMessage(char headerLength, byte[] keyId, int senderid, int receiverid, byte messageType, byte isDelete, int timestamp, int voiceLength, byte[] messageContent) {
        byte[] header = charToBytes(headerLength);
        byte[] sender = intToBytes(senderid);
        byte[] receiver = intToBytes(receiverid);
        byte[] time = intToBytes(timestamp);
        try {
            //byte[] encryptContent = EncryptUtils.encryptAES(messageContent, keyId);
            //byte[] message = new byte[headerLength + encryptContent.length];
            byte[] message = new byte[headerLength + messageContent.length];
            for (int i = 0; i < message.length; i++) {
                if (i < 2) {
                    message[i] = header[i];
                } else if (i < 38) {
                    message[i] = keyId[i - 2];
                } else if (i < 42) {
                    message[i] = sender[i - 38];
                } else if (i < 46) {
                    message[i] = receiver[i - 42];
                } else if (i < 47) {
                    message[i] = messageType;
                } else if (i < 48) {
                    message[i] = isDelete;
                } else if (i < 52) {
                    message[i] = time[i - 48];
                } else if (messageType == 3 && i == 52) {
                    message[i] = (byte) voiceLength;
                } else if (i >= headerLength) {
                    //message[i] = encryptContent[i - headerLength];
                    message[i] = messageContent[i - headerLength];
                }
            }
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] getMessage(char headerLength, byte[] keyId, int senderid, int receiverid, byte messageType, byte isDelete, int timestamp, byte[] messageContent) {
        byte[] header = charToBytes(headerLength);
        byte[] sender = intToBytes(senderid);
        byte[] receiver = intToBytes(receiverid);
        byte[] time = intToBytes(timestamp);
        try {
            //byte[] encryptContent = EncryptUtils.encryptAES(messageContent, keyId);
            //byte[] message = new byte[headerLength + encryptContent.length];
            byte[] message = new byte[headerLength + messageContent.length];
            for (int i = 0; i < message.length; i++) {
                if (i < 2) {
                    message[i] = header[i];
                } else if (i < 38) {
                    message[i] = keyId[i - 2];
                } else if (i < 42) {
                    message[i] = sender[i - 38];
                } else if (i < 46) {
                    message[i] = receiver[i - 42];
                } else if (i < 47) {
                    message[i] = messageType;
                } else if (i < 48) {
                    message[i] = isDelete;
                } else if (i < 52) {
                    message[i] = time[i - 48];
                } else if (i >= headerLength) {
                    //message[i] = encryptContent[i - headerLength];
                    message[i] = messageContent[i - headerLength];
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

    public static int getSenderId(byte[] message) {
        byte[] senderIdBytes = new byte[]{message[38], message[39], message[40], message[41]};
        return bytesToInt(senderIdBytes, 0);
    }

    public static int getReceiverId(byte[] message) {
        byte[] receiverIdBytes = new byte[]{message[42], message[43], message[44], message[45]};
        return bytesToInt(receiverIdBytes, 0);
    }

    public static void setTime(byte[] payload, long l) {
        byte[] bytes = intToBytes((int) l);
        for (int i = 0; i < 4; i++) {
            payload[48 + i] = bytes[i];
        }
    }

    public static byte[] getDecryptedMessage(byte[] payload, String key) {
        byte[] message = new byte[]{0};
        try {
            //plain = EncryptUtils.decryptAES(getMessageContent(message), getMessageKey(message));
            EnvelopEncInterface envelopEnc = EnvelopEnc.getInstance();
            List<QtRecipientInfo> list = new ArrayList();
            QtRecipientInfo qtRecipientInfo = new QtRecipientInfo();
            qtRecipientInfo.seteEncrypAlg(EEncrypAlg.AES256_CBC_PKCS5PADDING);
            QtKey qtKey = new QtKey();
            byte[] messageKeyId = getMessageKeyId(payload);
            byte[] keyBytes = key.getBytes();
            for (int i = 0; i < qtKey.getBuffer().length; i++) {
                qtKey.getBuffer()[i] = keyBytes[i];
            }
            qtRecipientInfo.setKey(qtKey);
            QtKeyId qtKeyId = new QtKeyId();
            for (int i = 0; i < qtKeyId.getBuffer().length; i++) {
                qtKeyId.getBuffer()[i] = messageKeyId[i];
            }
            qtRecipientInfo.setKeyId(qtKeyId);
            list.add(qtRecipientInfo);

            byte[] messageContent = getMessageContent(payload);
            ResultInfo resultInfo = envelopEnc.decryptByteArray(messageContent, qtKeyId, qtKey);
            messageContent = resultInfo.getValue();
            message = setMessageContent(payload, messageContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
}
