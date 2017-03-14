// IPlaybackService.aidl
package com.calf.player.services;

import com.calf.bean.MusicInfo;
import com.calf.player.services.IPlayDelegate;

interface IPlaybackService {

    void play(in MusicInfo musicInfo);
    void setPlayDelegate(IPlayDelegate delegate);

}
