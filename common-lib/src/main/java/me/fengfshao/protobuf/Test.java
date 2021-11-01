//package me.fengfshao.protobuf;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import me.fengfshao.protobuf.pb3.DynamicProtoBuilder;
//import scala.Tuple2;
//import org.apache.commons.codec.binary.Base64;
//
//
//import java.io.InputStream;
//import java.util.*;
//
///**
// * Author: shaoff
// * Date: 2021/10/19 19:20
// * Package: me.fengfshao.protobuf
// * Description:
// */
//public class Test {
//    static List<String> feedPbByteData=
//            Arrays.asList("Cg4xMDAwMDAwNjMxMjc5OBINMTYzNDU3NTM0NjI1MBgBIhBub25fc3Rhcl9wdWJsaXNoIhBjYXRlZ29yeV9ub19nYW1lIg50b3BpY2lkc19lbXB0eSIVY2F0ZWdvcnlfY2Zyb21fbm9fTkJBIg9mZWVkX3R5cGVfdmlkZW8iEW5vbl9zdGFyX2ludGVyYWN0IhVzY2VuZV91cGxvYWRfdGltZV8xMjAiD2NhdGVnb3J5X25vX05CQSoeCgRmZWVkEgYxMDAwMDMSBjEwMDAwMhIGMTAwMDA0MowBCg4xMDAwMDAwNjMxMjc5OBIBMDIGMzMzMzMzOLi95+eGLEgAUgtjMDAyMDFydWwxNFoPM2QyMGhmeHRhd2RxM2Q2agkxMjMxMjMxMjZyCjIxMDAwNjY2NDGKAQtnMTUzNXd2MGlzMegBytShoskv8gEBMboCCjIxMDAwNjY2NDHCAgEyigMBMYoDATQ6BwjK1KGiyS8=",
//                    "Cg4xMDAwMDAxMDE1ODI4NRINMTYzNDU3NTM0NjI2NBgBIhBub25fc3Rhcl9wdWJsaXNoIhBjYXRlZ29yeV9ub19nYW1lIg50b3BpY2lkc19lbXB0eSIVY2F0ZWdvcnlfY2Zyb21fbm9fTkJBIg9mZWVkX3R5cGVfdmlkZW8iEW5vbl9zdGFyX2ludGVyYWN0IhVzY2VuZV91cGxvYWRfdGltZV8xMjAiD2NhdGVnb3J5X25vX05CQSoeCgRmZWVkEgYxMDAwMDMSBjEwMDAwMhIGMTAwMDA0MowBCg4xMDAwMDAxMDE1ODI4NRIBMDIGMzMzMzMzOKimpemGLEgAUgtuMDAyNXd3eG82bVoPdnU5c3czbjd5cmg3cXdtagkxMjMxMjMxMjZyCjIxMDAwNjE4NjaKAQt5MTUzNWNlNTk0aegB2NShoskv8gEBMboCCjIxMDAwNjE4NjbCAgEyigMBMYoDATQ6BwjY1KGiyS8=",
//                    "Cg4xMDAwMDAyMjg4NzIxMxINMTYzNDU3NTM0NjI3NBgAIhBub25fc3Rhcl9wdWJsaXNoIhBjYXRlZ29yeV9ub19nYW1lIg50b3BpY2lkc19lbXB0eSIVY2F0ZWdvcnlfY2Zyb21fbm9fTkJBIgxlaWRfbm9fdmlkZW8iEW5vbl9zdGFyX2ludGVyYWN0IhVzY2VuZV91cGxvYWRfdGltZV8xMjAiDHNjZW5lX3N0YXR1cyIPY2F0ZWdvcnlfbm9fTkJBMpsBCg4xMDAwMDAyMjg4NzIxMxIBMyIc8J+MuPCfjYPwn4y48J+Ng/CfjYPwn4y48J+MuDIJMTc2Nzk4NDIzOOjgvu+GLEgAUgttMDAyNXd6NXAyYloPM2w4dGt3dWxucDg4bjk1agkxMjMxMjMxMzByCjIxMDAwNjE5NzjoAeLUoaLJL/IBATG6AgoyMTAwMDYxOTc4wgIBMooDATE6Bwji1KGiyS8=",
//                    "Cg4xMDAwMDAyMzA0Nzg0ORINMTYzNDU3NTM0NjI4MxgBIhBub25fc3Rhcl9wdWJsaXNoIhBjYXRlZ29yeV9ub19nYW1lIg50b3BpY2lkc19lbXB0eSIVY2F0ZWdvcnlfY2Zyb21fbm9fTkJBIg9mZWVkX3R5cGVfdmlkZW8iEW5vbl9zdGFyX2ludGVyYWN0IhVzY2VuZV91cGxvYWRfdGltZV8xMjAiD2NhdGVnb3J5X25vX05CQSoeCgRmZWVkEgYxMDAwMDMSBjEwMDAwMhIGMTAwMDA0Mo8BCg4xMDAwMDAyMzA0Nzg0ORIBMDIJNzYxMTYzNjE2OMD5yO+GLEgAUgtkMDAyNWcxN29lYloPcXN0YWh1bjBqczJpeXd4agkxMjMxMjMxMjZyCjIxMDAwNjU1NjWKAQt0MTUzNWw3dXZweugB69Shoskv8gEBMboCCjIxMDAwNjU1NjXCAgEwigMBMYoDATQ6Bwjr1KGiyS8=",
//                    "Cg4xMDAwMDAzMjE5OTc2NRINMTYzNDU3NTM0NjI5MBgBIhBub25fc3Rhcl9wdWJsaXNoIhBjYXRlZ29yeV9ub19nYW1lIg50b3BpY2lkc19lbXB0eSIVY2F0ZWdvcnlfY2Zyb21fbm9fTkJBIg9mZWVkX3R5cGVfdmlkZW8iEW5vbl9zdGFyX2ludGVyYWN0IhVzY2VuZV91cGxvYWRfdGltZV8xMjAiD2NhdGVnb3J5X25vX05CQSoeCgRmZWVkEgYxMDAwMDMSBjEwMDAwMhIGMTAwMDA0Mo8BCg4xMDAwMDAzMjE5OTc2NRIBMDIJMjMzNTcxNjQ0OOjYi/OGLEgAUgtzMDAyMHhqZm16Y1oPaDBtZWVwNnA3NjZqZ3FoagkxMjMxMjMxMjZyCjIxMDAwNDUyNTCKAQt4MTczNTU3NDNvZ+gB8tShoskv8gEBMboCCjIxMDAwNDUyNTDCAgE2igMBMYoDATQ6Bwjy1KGiyS8=",
//                    "Cg4xMDAwMDAzMjM2NTExMxINMTYzNDU3NTM0NjMwMhgBIhBub25fc3Rhcl9wdWJsaXNoIhBjYXRlZ29yeV9ub19nYW1lIg50b3BpY2lkc19lbXB0eSIVY2F0ZWdvcnlfY2Zyb21fbm9fTkJBIg9mZWVkX3R5cGVfdmlkZW8iEW5vbl9zdGFyX2ludGVyYWN0IhVzY2VuZV91cGxvYWRfdGltZV8xMjAiD2NhdGVnb3J5X25vX05CQSoeCgRmZWVkEgYxMDAwMDMSBjEwMDAwMhIGMTAwMDA0Mo8BCg4xMDAwMDAzMjM2NTExMxIBMDIJODU2MTE4NzA4OJCNkvOGLEgAUgs5QWg5SEI0S21zMloPc2E3cHAwbjRrejZjbmI5agkxMjMxMjMxMjZyCjIxMDAwNDUxMjaKAQtnMTUzNWs0b2FpZ+gB/tShoskv8gEBMboCCjIxMDAwNDUxMjbCAgEyigMBMYoDATQ6Bwj+1KGiyS8=");
//    // 测试两种写pb方式写新召回引擎结果对比
//    public static void testConvertIndexPb() throws Exception{
//        long timestamp=1634616461962L;
//
//        File file;
//        FileWriter fileWriter=new FileWriter("feed_pb_bytes.data");
//        for(String str:feedPbByteData){
//            FeedBuild.Info info= FeedBuild.Info.parseFrom(Base64.decodeBase64(str));
//            Tuple2<String, byte[]> res1 = fillNewFeedIndexV1Index(info, timestamp).get();
////        System.out.println(res1._1);
//
//            Map<String, Object> fields = extractFeedField(info, timestamp);
//            InputStream protoInput = Thread.currentThread().getContextClassLoader()
//                    .getResource("new_feed_index_v1.proto").openStream();
//
//            DynamicProtoBuilder.ProtoHolder.registerProto(protoInput,"FeedIndexV1Message");
//
//            byte[] bytes2=DynamicProtoBuilder.buildMessage("FeedIndexV1Message", fields).toByteArray();
//            byte[] bytes1=res1._2;
//            System.out.println(Arrays.equals(bytes1, bytes2));
//            NewFeedIndexV1Proto.FeedIndexV1Message info1= NewFeedIndexV1Proto.FeedIndexV1Message.parseFrom(bytes1);
//            NewFeedIndexV1Proto.FeedIndexV1Message info2= NewFeedIndexV1Proto.FeedIndexV1Message.parseFrom(bytes2);
//            fileWriter.write(info2.getFeedid()+" "+Base64.encodeBase64String(bytes2));
//            fileWriter.write("\n");
//
//            System.out.println(info1.equals(info2));
//            System.out.println(info1.getSceneUpdataTime());
//            System.out.println(info2.getSceneUpdataTime());
//            System.out.println(info1.getFeedid());
//
//        }
//
//        fileWriter.close();
//    }
//
//    private static Map<String,Object> extractFeedField(FeedBuild.Info info,long dataVersion){
//        Map<String,Object> fields=new HashMap<>();
//        FeedBuild.Hbase hbaseData = info.getHbase();
//        fields.put("feedid", info.getFeedid());
//        fields.put("data_version",dataVersion);
//
//        List<FeedBuild.Business> businessList = info.getBusinessList();
//        List<String> ztids = new ArrayList<>();
//        if (!businessList.isEmpty()) {
//            businessList.stream().forEach(business -> ztids.addAll(business.getZtidList()));
//        }
//        fields.put("ztid_list", ztids);
//        fields.put("ztid", joinWithSeparator(ztids, Separator.COMMA));
//        fields.put("topicids_list", hbaseData.getTopicidsList());
//        fields.put("topicids", joinWithSeparator(hbaseData.getTopicidsList(), Separator.COMMA));
//        fields.put("dokiids_list", hbaseData.getDokiidsList());
//        fields.put("dokiids", joinWithSeparator(hbaseData.getDokiidsList(), Separator.COMMA));
//
//        fields.put("scene_status", hbaseData.getSceneStatus());
//        fields.put("modify_time", hbaseData.getModifyTime());
//        fields.put("upload_under_vid", hbaseData.getUploadUnderVid());
//        fields.put("upload_under_cid", hbaseData.getUploadUnderCid());
//        fields.put("upload_under_lid", hbaseData.getUploadUnderLid());
//        fields.put("feed_type", joinWithSeparator(hbaseData.getFeedTypeList(), Separator.COMMA));
//        fields.put("feed_type_list", hbaseData.getFeedTypeList());
//
//        fields.put("original_feedid", hbaseData.getOriginalFeedid());
//        fields.put("feed_text", hbaseData.getFeedText());
//        fields.put("vuid", hbaseData.getVuid());
//
//        fields.put("scene_upload_time",hbaseData.getSceneUploadTime());
//        fields.put("scene_is_normalized",hbaseData.getSceneIsNormalized());
//        fields.put("scene_updata_time",hbaseData.getSceneUpdataTime());
//        /*fields.put("eid",hbaseData.getEid());
//        fields.put("cut_origin_vid",hbaseData.getCutOriginVid());
//        fields.put("cut_h5_url",hbaseData.getCutH5Url());
//        fields.put("scene_vid",hbaseData.getSceneVid());
//        fields.put("original_scene_status",hbaseData.getOriginalSceneStatus());
//        fields.put("scene_content_type",hbaseData.getSceneContentType());
//        fields.put("scene_attitude",hbaseData.getSceneAttitude());
//        fields.put("scene_tag", joinWithSeparator(hbaseData.getSceneTagList(), Separator.COMMA));
//        fields.put("scene_tag_list", hbaseData.getSceneTagList());
//        fields.put("scene_img",hbaseData.getSceneImg());
//        fields.put("user_type",hbaseData.getUserType());
//        fields.put("correlation_json",hbaseData.getCorrelationJson());
//        fields.put("ip_doki_correlation_json",hbaseData.getIpDokiCorrelationJson());
//        fields.put("like_num",hbaseData.getLikeNum());
//        fields.put("comment_num",hbaseData.getCommentNum());
//        fields.put("is_high_quality",hbaseData.getIsHighQuality());
//        fields.put("is_play_vote_scene",hbaseData.getIsPlayVoteScene());
//        fields.put("latest_comment_timestamp",hbaseData.getLatestCommentTimestamp());
//        fields.put("scene_incomplete",hbaseData.getSceneIncomplete());
//        fields.put("cfrom",hbaseData.getCfrom());
//        fields.put("is_significance",hbaseData.getIsSignificance());
//        fields.put("black_dokis",hbaseData.getBlackDokis());
//        fields.put("black_squares",hbaseData.getBlackSquares());
//        fields.put("black_topics",hbaseData.getBlackTopics());*/
//
//        return fields;
//    }
//    public static void main(String[] args) throws Exception {
//        testConvertIndexPb();
//    }
//
//    public static Optional<Tuple2<String, byte[]>> fillNewFeedIndexV1Index(FeedBuild.Info info,
//            long dataVersion) {
//
//        FeedBuild.Hbase hbaseData = info.getHbase();
//        NewFeedIndexV1Proto.FeedIndexV1Message.Builder newIndexBuilder = NewFeedIndexV1Proto.FeedIndexV1Message
//                .newBuilder();
//        newIndexBuilder.setFeedid(info.getFeedid());
//        newIndexBuilder.setDataVersion(dataVersion);
//
//        List<FeedBuild.Business> businessList = info.getBusinessList();
//        List<String> ztids = new ArrayList<>();
//        if (!businessList.isEmpty()) {
//            businessList.stream().forEach(business -> ztids.addAll(business.getZtidList()));
//        }
//        newIndexBuilder.addAllZtidList(ztids);
//
//        newIndexBuilder.setZtid(joinWithSeparator(ztids, Separator.COMMA));
//        newIndexBuilder.addAllTopicidsList(hbaseData.getTopicidsList());
//        newIndexBuilder.setTopicids(
//                joinWithSeparator(hbaseData.getTopicidsList(), Separator.COMMA));
//        newIndexBuilder.addAllDokiidsList(hbaseData.getDokiidsList());
//        newIndexBuilder.setDokiids(joinWithSeparator(hbaseData.getDokiidsList(), Separator.COMMA));
//        newIndexBuilder.setSceneStatus(hbaseData.getSceneStatus());
//        newIndexBuilder.setModifyTime(hbaseData.getModifyTime());
//        newIndexBuilder.setUploadUnderVid(hbaseData.getUploadUnderVid());
//        newIndexBuilder.setUploadUnderCid(hbaseData.getUploadUnderCid());
//        newIndexBuilder.setUploadUnderLid(hbaseData.getUploadUnderLid());
//        newIndexBuilder.setFeedType(
//                joinWithSeparator(hbaseData.getFeedTypeList(), Separator.COMMA));
//        newIndexBuilder.addAllFeedTypeList(hbaseData.getFeedTypeList());
//        newIndexBuilder.setOriginalFeedid(hbaseData.getOriginalFeedid());
//        newIndexBuilder.setFeedText(hbaseData.getFeedText());
//        newIndexBuilder.setVuid(hbaseData.getVuid());
//        newIndexBuilder.setSceneUploadTime(hbaseData.getSceneUploadTime());
//        newIndexBuilder.setSceneIsNormalized(hbaseData.getSceneIsNormalized());
//        newIndexBuilder.setSceneUpdataTime(hbaseData.getSceneUpdataTime());
//        /*newIndexBuilder.setEid(hbaseData.getEid());
//        newIndexBuilder.setCutOriginVid(hbaseData.getCutOriginVid());
//        newIndexBuilder.setCutH5Url(hbaseData.getCutH5Url());
//        newIndexBuilder.setSceneVid(hbaseData.getSceneVid());
//        newIndexBuilder.setOriginalSceneStatus(hbaseData.getOriginalSceneStatus());
//        newIndexBuilder.setSceneContentType(hbaseData.getSceneContentType());
//        newIndexBuilder.setSceneAttitude(hbaseData.getSceneAttitude());
//        newIndexBuilder.setSceneTag(
//                joinWithSeparator(hbaseData.getSceneTagList(), Separator.COMMA));
//        newIndexBuilder.addAllSceneTagList(hbaseData.getSceneTagList());
//        newIndexBuilder.setSceneImg(hbaseData.getSceneImg());
//        newIndexBuilder.setUserType(hbaseData.getUserType());
//        newIndexBuilder.setCorrelationJson(hbaseData.getCorrelationJson());
//        newIndexBuilder.setIpDokiCorrelationJson(hbaseData.getIpDokiCorrelationJson());
//        newIndexBuilder.setLikeNum(hbaseData.getLikeNum());
//        newIndexBuilder.setCommentNum(hbaseData.getCommentNum());
//        newIndexBuilder.setIsHighQuality(hbaseData.getIsHighQuality());
//        newIndexBuilder.setIsPlayVoteScene(hbaseData.getIsPlayVoteScene());
//        newIndexBuilder.setLatestCommentTimestamp(hbaseData.getLatestCommentTimestamp());
//        newIndexBuilder.setSceneIncomplete(hbaseData.getSceneIncomplete());
//        newIndexBuilder.setCfrom(hbaseData.getCfrom());
//        newIndexBuilder.setIsSignificance(hbaseData.getIsSignificance());
//        newIndexBuilder.setBlackDokis(hbaseData.getBlackDokis());
//        newIndexBuilder.setBlackSquares(hbaseData.getBlackSquares());
//        newIndexBuilder.setBlackTopics(hbaseData.getBlackTopics());*/
//        return Optional.of(new Tuple2<>(info.getFeedid(), newIndexBuilder.build().toByteArray()));
//    }
//
//    public static String joinWithSeparator(List<String> inputList, String sep) {
//        if (inputList==null||inputList.isEmpty()) {
//            return "";
//        }
//        return String.join(sep, inputList);
//    }
//}
