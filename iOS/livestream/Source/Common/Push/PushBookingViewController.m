//
//  PushBookingViewController.m
//  livestream
//
//  Created by Max on 2017/10/10.
//  Copyright © 2017年 net.qdating. All rights reserved.
//

#import "PushBookingViewController.h"
#import "LiveMutexService.h"
#import "LiveModule.h"
#import "LSUserInfoManager.h"
#import "LSImageViewLoader.h"
#import "LSTimer.h"
#import "LiveGobalManager.h"

@interface PushBookingViewController ()<UIAlertViewDelegate>
@property (nonatomic, strong) LSUserInfoManager *userInfoManager;
@property (nonatomic, strong) LSImageViewLoader *imageViewLoader;
@property (strong) LSTimer *removeTimer;
@end

@implementation PushBookingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.view.clipsToBounds = YES;
    self.view.layer.cornerRadius = 5;
    self.view.layer.masksToBounds = NO;
    
    self.userInfoManager = [LSUserInfoManager manager];
    self.imageViewLoader = [LSImageViewLoader loader];
    
    self.removeTimer = [[LSTimer alloc] init];
    
    self.tipsLabel.text = [NSString stringWithFormat:NSLocalizedStringFromSelf(@"PUSH_BROADCAST_TIP"),self.userId];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    LiveModule *module = [LiveModule module];
    if( [[LiveModule module].delegate respondsToSelector:@selector(moduleOnNotificationDisappear:)] ) {
        [[LiveModule module].delegate moduleOnNotificationDisappear:module];
    }
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self updataUserInfo:self.userId];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    self.ladyImageView.layer.cornerRadius = self.ladyImageView.bounds.size.width * 0.5;
    self.ladyImageView.clipsToBounds = YES;
    [[LiveModule module].analyticsManager reportActionEvent:ShowBooking eventCategory:EventCategoryGobal];
    // 定时3分钟移除
    [self timingRemoveView];
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    // 停止定时器
    [self.removeTimer stopTimer];
}

#pragma mark - 界面事件
- (IBAction)acceptAction:(id)sender {
    NSLog(@"PushBookingViewController::acceptAction: url:%@",self.url);
    
    if ([LiveGobalManager manager].isHangouting) {
        
        UIAlertView * alertView = [[UIAlertView alloc]initWithTitle:@"" message:NSLocalizedStringFromSelf(@"Alert_Msg") delegate:self cancelButtonTitle:NSLocalizedString(@"CANCEL", nil) otherButtonTitles:NSLocalizedString(@"SURE", nil), nil];
        [alertView show];
    }
    else
    {
        // 停止定时器
        [self.removeTimer stopTimer];
        // 跳转预约倒计时
        [[LiveMutexService service] openUrlByLive:self.url];
        // TODO:Google Analyze - ClickBooking
        [[LiveModule module].analyticsManager reportActionEvent:ClickBooking eventCategory:EventCategoryGobal];
        // 移除界面
        [self.view removeFromSuperview];
        [self removeFromParentViewController];
    }
}

#pragma mark - AlertViewDelegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == alertView.cancelButtonIndex) {
        // 停止定时器
        [self.removeTimer stopTimer];
        // 移除界面
        [self.view removeFromSuperview];
        [self removeFromParentViewController];
    }
    else
    {
        // 停止定时器
        [self.removeTimer stopTimer];
        // 跳转预约倒计时
        [[LiveMutexService service] openUrlByLive:self.url];
        // TODO:Google Analyze - ClickBooking
        [[LiveModule module].analyticsManager reportActionEvent:ClickBooking eventCategory:EventCategoryGobal];
        // 移除界面
        [self.view removeFromSuperview];
        [self removeFromParentViewController];
    }
}

- (IBAction)cancelAction:(id)sender {
    [self.removeTimer stopTimer];
    [self.view removeFromSuperview];
    [self removeFromParentViewController];
}

- (void)updataUserInfo:(NSString *)userId {
    WeakObject(self, weakSelf);
    [self.userInfoManager getUserInfo:userId finishHandler:^(LSUserInfoModel * _Nonnull item) {
        dispatch_async(dispatch_get_main_queue(), ^{
            weakSelf.tipsLabel.text = [NSString stringWithFormat:NSLocalizedStringFromSelf(@"PUSH_BROADCAST_TIP"),item.nickName];
            [weakSelf.imageViewLoader refreshCachedImage:weakSelf.ladyImageView options:SDWebImageRefreshCached imageUrl:item.photoUrl placeholderImage:[UIImage imageNamed:@"Default_Img_Lady_Circyle"]];
        });
    }];
}

- (void)timingRemoveView {
    WeakObject(self, weakSelf);
    [self.removeTimer startTimer:nil timeInterval:180.0 * NSEC_PER_SEC starNow:NO action:^{
        dispatch_async(dispatch_get_main_queue(), ^{
            [weakSelf cancelAction:nil];
        });
    }];
}

@end