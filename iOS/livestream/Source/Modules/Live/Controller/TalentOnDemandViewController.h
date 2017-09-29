//
//  TalentOnDemandViewController.h
//  livestream
//
//  Created by Calvin on 17/9/20.
//  Copyright © 2017年 net.qdating. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LiveRoom.h"
#import "GoogleAnalyticsViewController.h"
@protocol TalentOnDemandVCDelegate <NSObject>
@optional;
- (void)talentOnDemandVCCancelButtonDid;

- (void)onSendtalentOnDemandMessage:(NSAttributedString *)message;
@end

@interface TalentOnDemandViewController : GoogleAnalyticsViewController

@property (nonatomic, weak) id<TalentOnDemandVCDelegate> delegate;

@property (weak, nonatomic) IBOutlet UIButton *closeBtn;

@property (weak, nonatomic) IBOutlet UIView *failedView;

@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (nonatomic, strong) LiveRoom * liveRoom;

- (void)getTalentList:(NSString *)roomId;

@end