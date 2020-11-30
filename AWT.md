# AWT

**AWT**

AWT是java GUI编程的一个古老的库，对于GUI编程，一开始，Java的思路是：那简单啊，有原生控件干嘛不用，至于不跨平台的，就不支持呗，又坚持了原则，又回避了问题。  
这一代的gui库，awt，就此诞生。这使用peer的原则，映射到平台相关的UI控制，跨平台性差。这种方式取的是各个平台相关的交集，但对于想开发复杂点界面的人来说，就有麻烦了。想来个目录树吧，对不起，不支持；想来个进度条吧，对不起，不支持。

AWT教程：[https://www.ntu.edu.sg/home/ehchua/programming/java/J4a_GUI.html](https://www.ntu.edu.sg/home/ehchua/programming/java/J4a_GUI.html)

**Swing**

这样一来，Java自己也觉得说不过去了。但又要跨平台，又要提供丰富的控件支持，那就只有另起炉灶，开始用第二种思路：自己动手、丰衣足食，自己重写一套GUI控件，代替操作系统的原生控件。这一代的gui库，叫做swing。这也是一个想“彻底”解决问题的思路，但是要付出代价  
代价之一就是效率, 自己画出来的控件毕竟不能跟原生控件比效率，尤其是在早期Java优化还不够完善的时候。  
代价之二就是效果。自己画的控件毕竟只是模拟，还是会有细节差别。比如著名的毛玻璃效果，这不是简单套样式就能套出来的。

**SWT**

就这样，一帮人商量着，又琢磨出个新思路：做适配。平台有这个控件，就直接用，保证效率；没有，再造轮子，保证可用。就这样，swt问世。eclipse的gui就是基于此。
swt是赞，不过这属于改良，两个根本问题仍在：  
1. 跟操作系统api打交道不是Java的长项，效率仍然不能与c++等相提并论。
2. 到底要不要跨平台。如果要跨平台，swt接浏览器控件、接ActiveX控件的功能就成了形同虚设；而要是不想跨平台，又何必使用Java呢，.Net在一旁已经恭候多时了。

综上，如果一个GUI程序使用Java，通常都是有这些特征：
确实是想跨平台
对界面并没有太多效果的要求，界面效率也不是瓶颈
相比于其他GUI工具，开发人员对Java更为熟悉


**JavaFx**

基本脚本的布局（可结合使用JFoenix） [https://www.thoughtco.com/gui-2034108](https://www.thoughtco.com/gui-2034108)
Oracle's intention is to eventually replace Swing with JavaFX. Java 8, released in 2014, was the first release to include JavaFX in the core distribution.

JavaFx 教程：[https://code.makery.ch/zh-cn/library/javafx-tutorial/part3/](https://code.makery.ch/zh-cn/library/javafx-tutorial/part3/)

