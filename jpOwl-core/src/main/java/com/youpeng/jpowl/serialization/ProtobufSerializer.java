//
//import com.google.protobuf.InvalidProtocolBufferException;
//
//public class ProtobufSerializer {
//
//    public static byte[] serialize(MonitorData data) {
//        return data.toByteArray();
//    }
//
//    public static MonitorData deserialize(byte[] bytes) throws InvalidProtocolBufferException {
//        return MonitorData.parseFrom(bytes);
//    }
//}