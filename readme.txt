本软件采用java编写，利用Java提供的类库，对各种文件进行字节加密，主要采用AES算法对文件加密，用RSA算法对AES密钥进行加密，加密后的文件后缀为.AESEncrypt，
AES密钥解密后放到加密文件开头部分，后面是AES加密后的密文，接着把RSA私钥以序列化形式存放在RSAPrivateSerialize.16.1文件中。
MainWindow为可视化操作界面，运行后，
  加密： 
      加密文件直接点击加密文件按钮，选择文件进行加密，稍后会把源文件加密生成加密文件，接着把源文件删除掉，
  再在D盘生成RSAPrivateSerialize.16.1文件，注意把这个私钥文件保存好，因为每次打开软件加密文件都会产生一对新的RSA公私钥和AES密钥，所以如果丢失文件，那么
  将无法进行恢复。
  解密：
      解密文件的时候必须先选择好私钥文件，再去选择加密后的文件进行解密。
  因为加密的是字节，所以任何文件都可以进行加密，包括.mp4,.mp3,.exe等常见文件。
  任务完成后会有音乐提示声播放。
  
  该软件编写的动机是前一阵爆发的永恒之蓝病毒，是一个勒索软件，现在常见的勒索软件都是用AES对称加密算法对文件加密，用RSA非对称加密算法再对AES密钥进行加密，
  因为AES加密速度快，RSA加密更加安全，所以AES+RSA成为了一个常见的加密体系。通过编写这个小软件加深自己对密码学的认识，也从这次病毒事件中学习到点什么东西，
  作为一个计算机专业的学生不能对于行业内发生的事情无动于衷！
