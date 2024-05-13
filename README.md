## 完结撒花！

历时半个月总算是完结了

本项目因平台原因无法实现部分功能，故代码与官方版本有些许出入，本人对源代码进行修改使其达到平替效果

部分修改代码如下：

#### 1、新增菜品代码开发-文件上传部分

阿里云oos未注册，为了方便本人将代码修改为文件保存到桌面存储，具体代码如下

```
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);
//  阿里云对象存储服务失效，重新定义本地存储
//        try {
////            原始文件名
//            String originalFileName = file.getOriginalFilename();
////            截取文件后缀
//            String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
//            String newFileName = UUID.randomUUID().toString() + fileType;
//
//            String filePath = aliOssUtil.upload(file.getBytes(), newFileName);
//            return Result.success(filePath);
//        } catch (IOException e) {
//            log.error("文件上传失败",e);
//        }
        try {
//        file是临时文件对象，需要转存到指定位置，否则本次请求结束后会完成临时文件的删除
            log.info(file.toString());
//        1、获取原始文件名
            String getOriginalFilename = file.getOriginalFilename();
//        2、获取上传文件的后缀名
            String fileType = getOriginalFilename.substring(getOriginalFilename.lastIndexOf("."));
//        3、重新为文件生成文件名，使用uuid生成，防止文件名重复造成文件覆盖问题
            String filerename = UUID.randomUUID().toString() + fileType;
//        4、获取本机桌面地址，创建文件夹存放图片资源
            File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
            String desktopPath = desktopDir.getAbsolutePath();
            File image_temp = new File(desktopPath + "\\image_temp");
//        文件夹不存在则创建
            if (!image_temp.exists()) {
                image_temp.mkdir();
            }

//        5、将临时文件转存到指定位置
            try {
                file.transferTo(new File(image_temp + "\\" + filerename));
            } catch (Exception e) {
                e.printStackTrace();
            }
//        此处文件路径为nginx静态资源访问路径
//        在nginx服务器配置了映射，使得可以通过image/filename可以直接访问到桌面目录文件
            String filePath = "image/" + filerename;
            return Result.success(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
```

#### 2、微信订单支付-跳过微信支付过程直接进入订单详情

由于微信支付平台无法申请个人收款api，且申请企业级api成本巨大，此处对微信小程序前端界面进行修改，对后端参数传递部分也进行适当修改

微信前端文件：pages/pay/index.js

将注释：**支付详情		-----			订单倒计时**	之间的所有代码注释，将下列代码插入其中

```
              // 支付详情：无微信支付api改写
              // 支付详情
              handleSave: function handleSave() {
                var _this = this;
                if (this.timeout) {
                  (0, _api.cancelOrder)(this.orderId).then(function (res) {});
                  uni.redirectTo({
                    url: '/pages/details/index?orderId=' + this.orderId
                  });

                } else {
                  //如果支付成功进入成功页
                  clearTimeout(this.times);
                  var params = {
                    orderNumber: this.orderDataInfo.orderNumber,
                    payMethod: this.activeRadio === 0 ? 1 : 2
                  };

                  (0, _api.paymentOrder)(params).then(function (res) {
                    if (res.code === 1) {
                      console.log("hcs，支付了！")
                      wx.showModal({
                        title: '提示',
                        content: '支付成功',
                        success: function () {
                          uni.redirectTo({
                            url: '/pages/success/index?orderId=' + _this.orderId
                          });
                        }
                      })
                      console.log('支付成功!')
                    } else {
                      console.log("hcs，支付失败了！")
                      wx.showModal({
                        title: '提示',
                        content: res.msg
                      })
                    }
                  });
                }

              },

```

微信支付校验过程替换为

```
//      微信支付功能未完成，前端跳过微信支付校验相关过程，此处为自定义校验，若实现微信支付功能删除下列代码，恢复以上注释代码即可
        OrderPaymentVO vo = new OrderPaymentVO();
        vo.setNonceStr("666");
        vo.setPaySign("hhh");
        vo.setPackageStr("prepay_id=wx");
        vo.setSignType("RSA");
        vo.setTimeStamp("1670380960");
```

注释掉orderServiceImpl类中所有关于微信退款的过程，比如：

```
        //支付状态
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == Orders.PAID) {
            //用户已支付，需要退款
//            微信支付未实现
//            String refund = weChatPayUtil.refund(
//                    ordersDB.getNumber(),
//                    ordersDB.getNumber(),
//                    new BigDecimal(0.01),
//                    new BigDecimal(0.01));
            log.info("申请退款：");
        }
```

百度地图部分代码可不添加，不影响项目运行



本项目经过此修改后仅于windows主机、wsl - Ubuntu20-04 环境进行测试部署，未尝试于服务器部署

资源详情请见：https://github.com/Li10op/Web_Resources
