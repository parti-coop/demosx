select
proposal0_.ISSUE_ID as col_0_0_,
proposal0_.REG_DT as col_1_0_,
proposal0_.CHG_DT as col_2_0_,
user5_.USER_ID as col_3_0_,
user5_.USER_EMAIL as col_4_0_,
user5_.USER_NAME as col_5_0_,
user5_.USER_ID as col_6_0_,
user5_.USER_EMAIL as col_7_0_,
user5_.USER_NAME as col_8_0_, p
roposal0_.REG_IP as col_9_0_,
proposal0_.CHG_IP as col_10_0_,
proposal0_.OPINION_TYPE as col_11_0_,
category3_.CATE_ID as col_12_0_,
category3_.CATE_NAME as col_13_0_,
category3_.USE_YN as col_14_0_,
category3_.CATE_SEQ as col_15_0_,
issuestats4_.VIEW_CNT as col_16_0_,
issuestats4_.LIKE_CNT as col_17_0_,
issuestats4_.OPINION_CNT as col_18_0_,
issuestats4_.YES_CNT as col_19_0_,
issuestats4_.NO_CNT as col_20_0_,
issuestats4_.ETC_CNT as col_21_0_,
proposal0_.ADMIN_COMMENT_DT as col_22_0_,
proposal0_.ADMIN_COMMENT as col_23_0_,
user5_.USER_ID as col_24_0_,
user5_.USER_EMAIL as col_25_0_,
user5_.USER_NAME as col_26_0_,
proposal0_.ISSUE_STATUS as col_27_0_,
proposal0_.ISSUE_TITLE as col_28_0_,
proposal0_.ISSUE_CONTENT as col_29_0_
from TB_ISSUE proposal0_
inner join TB_USER user1_ on proposal0_.REG_ID=user1_.USER_ID
inner join TB_USER user2_ on proposal0_.CHG_ID=user2_.USER_ID
inner join TB_ISSUE_CATEGORY category3_ on proposal0_.CATE_ID=category3_.CATE_ID
inner join TB_ISSUE_STATS issuestats4_ on proposal0_.STATS_ID=issuestats4_.STATS_ID
left outer join TB_USER user5_ on proposal0_.MANAGER_ID=user5_.USER_ID
where proposal0_.ISSUE_DTYPE='P' and proposal0_.ISSUE_ID=?
