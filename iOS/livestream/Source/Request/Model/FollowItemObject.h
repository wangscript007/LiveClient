//
//  FollowItemObject.h
//  dating
//
//  Created by Alex on 17/8/17.
//  Copyright © 2017年 qpidnetwork. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <httpcontroller/HttpRequestEnum.h>

@interface FollowItemObject : NSObject
/**
 * Follow结构体
 * userId			 主播ID
 * nickName          主播昵称
 * photoUrl		     主播头像url
 * onlineStatus		 主播在线状态
 * roomName          直播间名称
 * loveLevel         亲密度等级
 * roomPhotoUrl		 直播间封面图url
 * roomType          直播间类型
 * addDate           添加收藏时间（1970年起的秒数）
 * interest          爱好ID列表
 */
@property (nonatomic, strong) NSString* userId;
@property (nonatomic, strong) NSString* nickName;
@property (nonatomic, strong) NSString* photoUrl;
// 直播间状态（0:离线（Offline） 正在直播（Live））
@property (nonatomic, assign) OnLineStatus onlineStatus;
@property (nonatomic, strong) NSString* roomName;
@property (nonatomic, strong) NSString* roomPhotoUrl;
// 直播间类型（0:（没有直播间） 1:（免费公开直播间） 2:（付费公开直播间） 3:（普通私密直播间） 4:（豪华私密直播间））
@property (nonatomic, assign) HttpRoomType roomType;
@property (nonatomic, assign) int loveLevel;
@property (nonatomic, assign) NSInteger addDate;
// 爱好ID列表
@property (nonatomic, strong) NSMutableArray<NSString*>* interest;

@end
