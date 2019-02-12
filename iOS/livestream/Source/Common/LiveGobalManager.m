//
//  LiveGobalManager.m
//  livestream
//
//  Created by Max on 2017/10/12.
//  Copyright © 2017年 net.qdating. All rights reserved.
//

#import "LiveGobalManager.h"

#import "LiveModule.h"
#import "LSImManager.h"

static LiveGobalManager *gManager = nil;
@interface LiveGobalManager () {
    BOOL _canShowInvite;
}
#pragma mark - 前后台切换逻辑
@property (assign) BOOL isBackground;
@property (strong) NSDate *enterBackgroundTime;
@property (strong) NSDate *startTime;
@property (assign) UIBackgroundTaskIdentifier bgTask;
@property (nonatomic, strong) NSMutableArray *delegates;
/**
 直播间弹出的导航栏
 */
@property (nonatomic, weak) LSNavigationController *liveRoomNVC;
@end
;

@implementation LiveGobalManager
#pragma mark - 获取实例
+ (instancetype)manager {
    if (gManager == nil) {
        gManager = [[[self class] alloc] init];
    }
    return gManager;
}

- (id)init {
    NSLog(@"LiveGobalManager::init()");

    if (self = [super init]) {
        self.delegates = [NSMutableArray array];

        // 注册前后台切换通知
        _isBackground = NO;
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(willEnterBackground:) name:UIApplicationWillResignActiveNotification object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(willEnterForeground:) name:UIApplicationDidBecomeActiveNotification object:nil];

        _canShowInvite = YES;

        _isHangouting = NO;
    }
    return self;
}

- (void)dealloc {
    NSLog(@"LiveGobalManager::dealloc()");

    // 注销前后台切换通知
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIApplicationWillResignActiveNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIApplicationDidBecomeActiveNotification object:nil];
}

- (void)addDelegate:(id<LiveGobalManagerDelegate>)delegate {
    @synchronized(self) {
        [self.delegates addObject:[NSValue valueWithNonretainedObject:delegate]];
    }
}

- (void)removeDelegate:(id<LiveGobalManagerDelegate>)delegate {
    @synchronized(self) {
        for (NSValue *value in self.delegates) {
            id<LiveGobalManagerDelegate> item = (id<LiveGobalManagerDelegate>)value.nonretainedObjectValue;
            if (item == delegate) {
                [self.delegates removeObject:value];
                break;
            }
        }
    }
}

- (BOOL)canShowInvite:(NSString *)uesrId {
    BOOL bFlag = NO;

    if (_canShowInvite) {
        if (self.liveRoom) {
            // 当前存在直播间
            if (self.liveRoom.roomType == LiveRoomType_Public || self.liveRoom.roomType == LiveRoomType_Public_VIP) {
                // 当前是公开直播间, 是否和邀请的主播Id一样
                if ([self.liveRoom.userId isEqualToString:uesrId]) {
                    bFlag = YES;
                }
            } else {
                // 其他直播间
            }
        } else {
            // 不存在直播间
            bFlag = YES;
        }
    }

    return bFlag;
}

- (void)setCanShowInvite:(BOOL)canShowInvite {
    _canShowInvite = canShowInvite;
}

#pragma mark - 直播间界面层级处理
- (void)presentLiveRoomVCFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC {
    // TODO:创建直播间界面层级
    NSLog(@"LiveGobalManager::presentLiveRoomVCFromVC( fromVC : %@, toVC : %@ )", [fromVC class].description, [toVC class].description);
    
    LSNavigationController *nvc = [[LSNavigationController alloc] initWithRootViewController:toVC];
    nvc.flag = YES;
    nvc.navigationBar.tintColor = fromVC.navigationController.navigationBar.tintColor;
    nvc.navigationBar.barTintColor = fromVC.navigationController.navigationBar.barTintColor;
    nvc.navigationBar.backgroundColor = fromVC.navigationController.navigationBar.backgroundColor;
    NSDictionary *attributes = [NSDictionary dictionaryWithObjectsAndKeys:[UIColor blackColor], NSForegroundColorAttributeName, nil];
    [nvc.navigationBar setTitleTextAttributes:attributes];
    [nvc.navigationItem setHidesBackButton:YES];

    self.liveRoomNVC = nvc;
    [fromVC.navigationController presentViewController:nvc animated:NO completion:nil];
}

- (void)dismissLiveRoomVC {
    // TODO:关闭直播间界面层级
    NSLog(@"LiveGobalManager::dismissLiveRoomVC()");
    
    self.liveRoomNVC = nil;

    UIViewController *moduleVC = [LiveModule module].moduleVC;
    if (moduleVC.navigationController) {
        UIViewController *vc = moduleVC.navigationController.topViewController;
        [vc dismissViewControllerAnimated:NO completion:nil];
    }
}

- (void)pushVCWithCurrentNVCFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC {
    // TODO:根据当前的导航栏推进界面
    NSLog(@"LiveGobalManager::pushVCWithCurrentNVCFromVC( fromVC : %@, toVC : %@ )", [fromVC class].description, [toVC class].description);
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        UINavigationController *nvc = self.liveRoomNVC;

        if (!nvc) {
            nvc = fromVC.navigationController;
        }

        if (nvc) {
            [nvc pushViewController:toVC animated:NO];
        }
    });
}

- (void)popToRootVC {
    // TODO:主导航栏推出到主界面
    NSLog(@"LiveGobalManager::popToRootVC()");
    
    UIViewController *moduleVC = [LiveModule module].moduleVC;
    if (moduleVC.navigationController) {
        UINavigationController *nvc = moduleVC.navigationController;
        [nvc popToRootViewControllerAnimated:NO];
    }
}

#pragma mark - 后台处理
- (void)willEnterBackground:(NSNotification *)notification {
    if (_isBackground == NO) {
        _isBackground = YES;

        NSLog(@"LiveGobalManager::willEnterBackground()");

        // 进入后台时间
        self.enterBackgroundTime = [NSDate date];

        UIApplication *app = [UIApplication sharedApplication];
        self.bgTask = [app beginBackgroundTaskWithName:@"GobalLiveBgTask"
                                     expirationHandler:^{
                                         // Clean up any unfinished task business by marking where you
                                         // stopped or ending the task outright.
                                         NSLog(@"LiveGobalManager::willEnterBackground( [GobalLiveBgTask expired] )");

                                         if (self.bgTask != UIBackgroundTaskInvalid) {
                                             [app endBackgroundTask:self.bgTask];
                                             self.bgTask = UIBackgroundTaskInvalid;
                                         }
                                     }];

        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
            // Do the work associated with the task, preferably in chunks.

            while (self.isBackground) {
                NSDate *now = [NSDate date];
                NSTimeInterval timeInterval = [now timeIntervalSinceDate:self.enterBackgroundTime];
                NSUInteger bgTime = timeInterval;
                NSTimeInterval enterRoomTimeInterval = 0;
                if (self.enterRoomBackgroundTime) {
                    enterRoomTimeInterval = [now timeIntervalSinceDate:self.enterRoomBackgroundTime];
                }
                NSUInteger enterRoomBgTime = enterRoomTimeInterval;

                NSLog(@"LiveGobalManager::willEnterBackground( bgTime : %lu, enterRoomBgTime : %lu )", (unsigned long)bgTime, (unsigned long)enterRoomBgTime);

                // 后台进入直播间超过60秒
                if (enterRoomBgTime > BACKGROUND_TIMEOUT) {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        for (NSValue *value in self.delegates) {
                            id<LiveGobalManagerDelegate> delegate = value.nonretainedObjectValue;
                            if ([delegate respondsToSelector:@selector(enterBackgroundTimeOut:)]) {
                                [delegate enterBackgroundTimeOut:self.enterRoomBackgroundTime];
                            }
                        }
                    });
                    break;
                }
                sleep(1);
            }

            if (self.bgTask != UIBackgroundTaskInvalid) {
                [app endBackgroundTask:self.bgTask];
                self.bgTask = UIBackgroundTaskInvalid;
            }

        });
    }
}

- (void)willEnterForeground:(NSNotification *)notification {
    if (_isBackground == YES) {
        _isBackground = NO;

        self.enterRoomBackgroundTime = nil;
        
        NSLog(@"LiveGobalManager::willEnterForeground()");
    }
}

@end