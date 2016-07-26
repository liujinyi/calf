package com.calf.frame;

import com.calf.frame.log.Logger;
import com.calf.frame.message.MessageManager;
import com.calf.frame.tool.Assert;

/**
 * @author JinYi Liu
 */
public final class FrameCore {

    public static void init() {
        MessageManager.init();
        Assert.setDebug(true);
        Logger.changeLog(Logger.Type.DEBUG);
    }

}
