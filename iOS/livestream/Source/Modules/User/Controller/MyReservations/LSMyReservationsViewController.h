//
//  LSMyReservationsViewController.h
//  livestream
//
//  Created by Calvin on 17/10/9.
//  Copyright © 2017年 net.qdating. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LSGoogleAnalyticsPageViewController.h"

@interface LSMyReservationsViewController : LSGoogleAnalyticsPageViewController
@property (nonatomic, assign) NSInteger curIndex;
- (void)getunreadCount;
@end