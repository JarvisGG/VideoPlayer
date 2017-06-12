# VideoPlayer
1.TextureView + MediaPlayer 封装到VideoPlayer
2.将视频交互页面操作封装到顶层View VideoPlayerController
3.通过VideoPlayerManager 来托管当前列表中处于播放的 VideoPlayer，在当前View detach出window的时候释放资源等等
4.添加手势滑动：目前全屏状态下支持左侧上滑控制音量，右侧上滑控制亮度


***后续添加滑动进度等等
