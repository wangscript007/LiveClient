//
//  LSUpQnInviteIdRequest.m
//  dating
//
//  Created by Alex on 19/7/29.
//  Copyright © 2019年 qpidnetwork. All rights reserved.
//

#import "LSUpQnInviteIdRequest.h"

@implementation LSUpQnInviteIdRequest
- (instancetype)init{
    if (self = [super init]) {
        self.manId = @"";
        self.anchorId = @"";
        self.inviteId = @"";
        self.roomId = @"";
        self.inviteType = LSBUBBLINGINVITETYPE_UNKNOW;
    }
    
    return self;
}

- (void)dealloc {
    
}

- (BOOL)sendRequest {
    if( self.manager ) {
        __weak typeof(self) weakSelf = self;
        NSInteger request = [self.manager upQnInviteId:self.manId anchorId:self.anchorId inviteId:self.inviteId roomId:self.roomId inviteType:self.inviteType finishHandler:^(BOOL success, HTTP_LCC_ERR_TYPE errnum, NSString *errmsg) {
            BOOL bFlag = NO;
            
            // 没有处理过, 才进入LSSessionRequestManager处理
            if( !weakSelf.isHandleAlready && weakSelf.delegate && [weakSelf.delegate respondsToSelector:@selector(request:handleRespond:errnum:errmsg:)] ) {
                bFlag = [self.delegate request:weakSelf handleRespond:success errnum:errnum errmsg:errmsg];
                weakSelf.isHandleAlready = YES;
            }
            
            if( !bFlag && weakSelf.finishHandler ) {
                weakSelf.finishHandler(success, errnum, errmsg);
                [weakSelf finishRequest];
            }
        }];
        return request != 0;
    }
    return NO;
}

- (void)callRespond:(BOOL)success errnum:(HTTP_LCC_ERR_TYPE)errnum errmsg:(NSString* _Nullable)errmsg {
    if( self.finishHandler && !success ) {
        self.finishHandler(NO, errnum, errmsg);
    }
    
    [super callRespond:success errnum:errnum errmsg:errmsg];
}

@end
