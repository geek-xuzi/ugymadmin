###浅析Java热修复

- 简介

     <p>我们知道大多数的 JVM 具备 Java 的 HotSwap 特性,利用这一特性,我们可以不重启 
     Java 进程条件下,改变 Java 方法的实现。通过这种方式,不用停止运行程序,就可以扩展在
     线的应用程序,或者在运行的项目上修复小的错误。</p>
     <p>假设有一个正在运行的应用程序，通过校验 HTTP 请求中的 X-Priority 头部，来执行
     服务器的特殊处理。该校验使用下面的工具类来实现:</p>
    
        class HeaderUtility {
           
               static boolean isPriorityCall(HttpServletRequest request) {
                   return request.getHeader("X-Pirority") != null;
               }
                 
        }
    __
     <p>修复这样的错误是很简单的。但是如果项目的重新部署很复杂,甚至不允许停机,带着错误运行可能会更好
     。那么 HotSwap 就给我们提供了另一种解决方案：可以在不重启应用的情况下进行小幅的改动。</p>

- Attach API
    
     <p>为了修改一个运行中的 Java 程序,需要一种可以和处在运行状态的 JVM 进行通信的方式。因为 
     Java 的虚拟机实现是一个受到管理的系统,因此拥有进行这些操作的标准 API。提问中涉及到的 API 被称作
     attachment API,它是官方 Java 工具的一部分。使用这个由运行之中的 JVM 所暴露的 API,能让第二个
     Java 进程来同其进行通信。</p>
     
     <p>其实，我们已经用到了该 API: 它已经由诸如 VisualVM 或者 Java Mission Control 这样的调试
     和模拟工具进行了应用。应用这些附件的 API 并没有同日常使用的标准 Java API 打包在一起，而是被打包到
     了一个特殊的文件之中，叫做 tools.jar，它只包含了一个虚拟机的 JDK 打包发布版本。更糟糕的是，这个 JAR 
     文件的位置并没有进行设置，它在 Windows、Linux，特别是在 Macintosh 上的 VM 都存在差别，不光文件的
     位置，连文件名也各异，有些发行版上就被叫做 classes.jar。最后，IBM 甚至决定对这个 JAR 中包含的一些类
     的名称进行修改，将所有 com.sun 类挪到 com.ibm 命名空间之中, 又添了一个乱子。在 Java 9 中，乱糟糟的
     状态才最终得以清理，tools.jar 被 Jigsaw 的模块 jdk.attach 所替代。</p>