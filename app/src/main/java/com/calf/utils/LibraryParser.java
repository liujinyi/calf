package com.calf.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Xml;

import com.calf.bean.AlbumInfo;
import com.calf.bean.ArtistInfo;
import com.calf.bean.MusicInfo;
import com.calf.bean.MvInfo;
import com.calf.bean.OnlineInfo;
import com.calf.bean.RadioInfo;
import com.calf.bean.RootInfo;
import com.calf.bean.SectionInfo;
import com.calf.bean.SongListInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by JinYi Liu on 17-2-9.
 */

public class LibraryParser {

    // Section type
    private static final String TYPE_MV = "mv";
    private static final String TYPE_LIST = "list";
    private static final String TYPE_MUSIC = "music";
    private static final String TYPE_SQUARE = "square";
    private static final String TYPE_SQUARE_3S = "3s";
    private static final String TYPE_SQUARE_4S = "4s";
    private static final String TYPE_SQUARE_BUSINESS = "business";
    private static final String TYPE_RECOM_EXTENSION = "bus_extension";
    private static final String TYPE_BANNER = "banner";
    private static final String TYPE_MV_SQUARE = "mvsquare";
    private static final String TYPE_PAN_BANNER = "panBanner";
    private static final String TYPE_PAN_SQUARE = "panSquare";
    private static final String TYPE_PAN_TAG_SQUARE = "panTagSquare";
    private static final String TYPE_ARTIST_LIST = "artistlist";//ui 1.0 added
    private static final String TYPE_USER_6S = "user_6s";//ui 1.0 added
    private static final String TYPE_KSQUARE = "ksquare";
    private static final String TYPE_KLIST = "klist";
    private static final String TYPE_KGRID = "kgeplaylist";
    private static final String TYPE_ROUND_3S = "round";  //圆形电台
    private static final String TYPE_TANGRAM = "tangram"; //上面一个大图，下面两个小图，然后是list
    //导航栏数据
    private static final String TYPE_MY_MODULE = "my_module";
    private static final String TYPE_ADD_MORE = "add_more";

    // XML 标签头
    private static final String TAG_ROOT = "root";
    private static final String TAG_SECTION = "section";
    private static final String TAG_AD = "ad";
    private static final String TAG_MV = "mv";
    private static final String TAG_APP = "app";
    private static final String TAG_LIST = "list";
    private static final String TAG_MVPL = "mvpl";
    private static final String TAG_AD_AR = "ad_ar";
    private static final String TAG_RADIO = "radio";
    private static final String TAG_ALBUM = "album";
    private static final String TAG_MUSIC = "music";
    private static final String TAG_SONGLIST = "Songlist";
    private static final String TAG_PICTORIAL = "pictorial";//8.0.0.0增加的音乐画报
    private static final String TAG_SONGLIST_RCM1 = "songlist_rcm1";//私人口味
    private static final String TAG_SONGLIST_RCM2 = "songlist_rcm2";//发现好歌
    private static final String TAG_PANCONTENT = "pancontent";
    private static final String TAG_ARTIST = "artist";
    private static final String TAG_GAME = "game";
    private static final String TAG_BILLBOARD = "Billboard";// 排行榜
    private static final String TAG_KUBILLBOARD = "kubillboard";// 酷音乐排行榜
    private static final String TAG_HITBILLBOARD = "hitbillboard";// 酷音乐排行榜
    private static final String TAG_RING = "ring";// 彩铃
    private static final String TAG_RINGPL = "ringpl";
    private static final String TAG_UNICOM_FLOW = "unicombag";// 流量包
    private static final String TAG_TAB = "tab";// Tab
    private static final String TAG_TEMPLATE_CHILD_INFO = "info";// 儿歌专区信息展示
    private static final String TAG_USER_INFO = "u_info"; //用户信息
    private static final String TAG_RADIO_LIST = "radiolist";//时段电台
    private static final String TAG_CD_BAG = "cdbag";//cd包
    private static final String TAG_SIGN_IN = "sign_in";//签到
    public static final String TAG_KSING_HALL = "ksing_hall"; //跳转k歌首页
    public static final String TAG_K_SONG_LIST = "kge_songlist";//k歌歌单
    private static final String TYPE_GAME_LIST = "gamelist";// 游戏大厅<游戏大厅>
    private static final String TYPE_FOCUSSKIN = "focusskin";// 焦点图换肤
    private static final String TYPE_RECAD = "recad";// 广告
    private static final String TYPE_AUTO_TAG = "autotag";// 自动化数据
    private static final String TYPE_TEMPLATE_AREA = "qz_list";// 模板化专区<服务器专区拼音写反，就成这样了>
    private static final String TYPE_GAME_H5 = "game_h5jump";
    private static final String TYPE_GAME_NATIVE = "game_nativejump";
    public static final String TYPE_SHOW = "show2";//秀场
    public static final String TYPE_GAMEHALL = "game_hall";//跳转到游戏大厅
    public static final String TYPE_KSING = "ksong";//K歌比赛
    public static final String TYPE_KPRODUCTION = "kproduction";//k歌作品
    public static final String TYPE_KOMNIBUS = "komnibus";//k歌曲精选集
    public static final String TYPE_BIBI = "bibi";//哔哔推荐

    //电台
    private final static String TAG_CHANEL = "chanel";
    // Section
    private static final String ATTR_START = "start";
    private static final String ATTR_COUNT = "count";
    private static final String ATTR_TOTAL = "total";
    private static final String ATTR_LABEL = "label";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_MID = "mid";
    private static final String ATTR_MDIGEST = "mdigest";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_APP_DESC = "app_desc";
    private static final String ATTR_APP_URL = "app_url";
    private static final String ATTR_AD_TEXT = "ad_text";
    private static final String ATTR_ANDROID_URL = "android_url";
    private static final String ATTR_ACTION = "action";
    private static final String ATTR_IMG = "img";
    private static final String ATTR_AR_URL = "ar_url";
    private static final String ATTR_AD_TYPE = "ad_type";
    private static final String ATTR_MDATA = "mdata";
    private static final String ATTR_MTYPE = "mtype";
    private static final String ATTR_SECTION_ID = "section_id";

    // Songlist
    private static final String ATTR_ID = "id";
    private static final String ATTR_SMALL_IMG = "small_img";
    private static final String ATTR_DESC = "desc";
    private static final String ATTR_DIGEST = "digest";
    private static final String ATTR_INFO = "info";
    private static final String ATTR_EXTEND = "extend";
    private static final String ATTR_ISNEW = "isnew";
    private static final String ATTR_FLAGTYPE = "flagtype";

    private static final String ATTR_BAN_URL = "ban_url";
    private static final String ATTR_BANNER_TXT = "banner_txt";
    private static final String ATTR_BANNER_IMG = "banner_img";
    private static final String ATTR_BANNER_URL = "banner_url";
    private static final String ATTR_OPEN_TYPE = "open_type";

    private static final String ATTR_LONG_PIC = "long_pic";
    private static final String ATTR_COMMENT_CNT = "commentcnt";    //评论数
    private static final String ATTR_LISTEN_CNT = "listencnt";    //听歌数

    // AD or AD_AR
    private static final String ATTR_VERSION = "version";
    private static final String ATTR_PUBLISH = "publish";
    private static final String ATTR_PROVIDER = "provider";
    private static final String ATTR_URL = "url";
    private static final String ATTR_INAPP = "inapp";
    public static final String ATTR_THIRDPARTY = "isThirdParty";

    // Radio
    private static final String ATTR_RADIO_ID = "radio_id";
    private static final String ATTR_LISTENCNT = "listencnt";

    // Album
    private static final String ATTR_ARTIST = "artist";
    private static final String ATTR_COMPANY = "company";
    private static final String ATTR_HOT = "hot";
    private static final String ATTR_ARTIST_ID = "artist_id";
    private static final String ATTR_UPDATE_TIME = "update_time";
    // Mv
    private static final String ATTR_RID = "rid";
    private static final String ATTR_DURATION = "duration";
    private static final String ATTR_FORMAT = "format";
    private static final String ATTR_RES = "res";
    private static final String ATTR_MINFO = "minfo";
    private static final String ATTR_MV_FLAG = "mvflag";
    private static final String ATTR_MV_QUALITY = "mvquality";
    private static final String ATTR_KMARK = "kmark";
    private static final String ATTR_TREND = "trend";
    private static final String ATTR_UPLOADER = "uploader";
    private static final String ATTR_UPTIME = "uptime";

    // MusicInfo
    private static final String ATTR_ALBUM = "album";
    private static final String ATTR_PAY_FLAG = "pay_flag";
    private static final String ATTR_SHOWTYPE = "showtype";
    private static final String ATTR_DISABLE = "disable";
    private static final String ATTR_AUDIO_ADID = "audio_id";
    private static final String ATTR_FLOAT_ADID = "float_adid";
    private static final String ATTR_PIC_LABEL = "pic_label";

    // Artist
    private static final String ATTR_MVCNT = "mvcnt";
    private static final String ATTR_ALBUMCNT = "albumcnt";
    private static final String ATTR_MUSICCNT = "musiccnt";
    private static final String ATTR_FANSCNT = "followers";

    // autotag
    private final static String ATTR_DATA = "data";

    //歌单用户信息
    private final static String ATTR_UID = "uid";


    private final static String ATTR_LOSSLESS = "lossless_mark";

    public static RootInfo parse(Context context, String data) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        parser.setInput(inputStream, "UTF-8");
        return parser(context, parser);
    }

    private static RootInfo parser(Context context, XmlPullParser parser)
            throws XmlPullParserException, IOException {
        RootInfo rootInfo = null;
        SectionInfo section = null;
        int event = parser.getEventType();// 产生第一个事件
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
                    break;
                case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
                    if (TAG_ROOT.equalsIgnoreCase(parser.getName())) {
                        rootInfo = new RootInfo();
                    } else if (TAG_SECTION.equalsIgnoreCase(parser.getName())) {
                        section = getSection(context, parser);
                    } else if (TAG_AD.equalsIgnoreCase(parser.getName())) {

                    } else if (TAG_AD_AR.equalsIgnoreCase(parser.getName())) {

                    } else if (TAG_SONGLIST.equalsIgnoreCase(parser.getName())) {
                        SongListInfo listInfo = getSongListInfo(context, parser);
                        section.addOnlineInfo(listInfo);
                    } else if (TAG_RADIO.equalsIgnoreCase(parser.getName())) {
                        RadioInfo radioInfo = getRadioInfo(context, parser);
                        section.addOnlineInfo(radioInfo);
                    } else if (TAG_ALBUM.equalsIgnoreCase(parser.getName())) {
                        AlbumInfo albumInfo = getAlbumInfo(context, parser);
                        section.addOnlineInfo(albumInfo);
                    } else if (TAG_MV.equalsIgnoreCase(parser.getName())) {
                        MvInfo mvInfo = getMvInfo(context, parser);
                        section.addOnlineInfo(mvInfo);
                    } else if (TAG_MVPL.equalsIgnoreCase(parser.getName())) {
//                        MvPlInfo mvPlInfo = getMvPlInfo(context, parser);
//                        section.addSection(mvPlInfo);
                    } else if (TAG_LIST.equalsIgnoreCase(parser.getName())) {
//                        BaseQukuItemList listInfo = getListInfo(context, parser);
//                        section.addSection(listInfo);
                    } else if (TAG_MUSIC.equalsIgnoreCase(parser.getName())) {
                        MusicInfo musicInfo = getMusicInfo(context, parser);
                        section.addOnlineInfo(musicInfo);
                    } else if (TAG_BILLBOARD.equalsIgnoreCase(parser.getName())) {
//                        billboardInfo = getBillboardInfo(context, parser);
//                        section.addSection(billboardInfo);
                    } else if (TAG_ARTIST.equalsIgnoreCase(parser.getName())) {
                        ArtistInfo artistInfo = getArtistInfo(context, parser);
                        section.addOnlineInfo(artistInfo);
                    } else if (TYPE_TEMPLATE_AREA.equalsIgnoreCase(parser.getName())) {

                    } else if (TYPE_AUTO_TAG.equalsIgnoreCase(parser.getName())) {

                    }
                    break;
                case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
                    if (TAG_SECTION.equals(parser.getName())) {
                        rootInfo.addSection(section);
                    }
                    break;
            }
            event = parser.next();// 进入下一个元素并触发相应事件
        }
        return rootInfo;
    }

    private static SectionInfo getSection(Context context, XmlPullParser parser) {
        SectionInfo sectionInfo = new SectionInfo();
        String type = getFormatAttributeValue(parser, ATTR_TYPE);
        sectionInfo.setType(type);
        int id = getDefaultInteger(parser, ATTR_ID, 0);
        sectionInfo.setId(id);
        int start = getDefaultInteger(parser, ATTR_START, 0);
        sectionInfo.setStart(start);
        int count = getDefaultInteger(parser, ATTR_COUNT, 0);
        sectionInfo.setCount(count);
        int total = getDefaultInteger(parser, ATTR_TOTAL, 0);
        sectionInfo.setTotal(total);
        return sectionInfo;
    }

    private static <T extends OnlineInfo> T parserBaseQukuItem(T info, XmlPullParser parser) {
        int id = getDefaultInteger(parser, ATTR_ID, 0);
        info.setId(id);
        String name = getFormatAttributeValue(parser, ATTR_NAME);
        info.setName(name);
        String img = getFormatAttributeValue(parser, ATTR_IMG);
        info.setImg(img);
        String extend = getFormatAttributeValue(parser, ATTR_EXTEND);
        info.setExtend(extend);
        String desc = getFormatAttributeValue(parser, ATTR_DESC);
        info.setDesc(desc);
        String digest = getFormatAttributeValue(parser, ATTR_DIGEST);
        info.setDigest(digest);
        return info;
    }

    private static MusicInfo getMusicInfo(Context context, XmlPullParser xmlParser) {
        MusicInfo info = new MusicInfo();
        parserBaseQukuItem(info, xmlParser);
        int rid = getDefaultInteger(xmlParser, ATTR_RID, 0);
        info.setId(rid);
        long playFlag = getDefaultLong(xmlParser, ATTR_PAY_FLAG, 0);
        info.setPayFlag(rid);
        String artist = getFormatAttributeValue(xmlParser, ATTR_ARTIST);
        info.setArtist(artist);
        String album = getFormatAttributeValue(xmlParser, ATTR_ALBUM);
        info.setAlbum(album);
        int duration = getDefaultInteger(xmlParser, ATTR_DURATION, 0);
        info.setDuration(Integer.valueOf(duration));
        String mvFlag = getFormatAttributeValue(xmlParser, ATTR_MV_FLAG);
        info.setMvFlag(mvFlag);
        String mvQuality = getFormatAttributeValue(xmlParser, ATTR_MV_QUALITY);
        info.setMvQuality(mvQuality);
        return info;
    }

    private static ArtistInfo getArtistInfo(Context context, XmlPullParser parser) {
        return null;
    }

    private static RadioInfo getRadioInfo(Context context, XmlPullParser parser) {
        return null;
    }

    private static MvInfo getMvInfo(Context context, XmlPullParser parser) {
        return null;
    }

    private static AlbumInfo getAlbumInfo(Context context, XmlPullParser parser) {
        return null;
    }

    private static SongListInfo getSongListInfo(Context context, XmlPullParser parser) {
        return null;
    }

    private static String getFormatAttributeValue(XmlPullParser parser, String name) {
        String str = parser.getAttributeValue(null, name);
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        return str;
    }

    private static int getDefaultInteger(XmlPullParser parser, String name, int value) {
        try {
            value = Integer.valueOf(getFormatAttributeValue(parser, name));
        } catch (Exception e) {
        }
        return value;
    }

    private static long getDefaultLong(XmlPullParser parser, String name, long value) {
        try {
            value = Long.valueOf(getFormatAttributeValue(parser, name));
        } catch (Exception e) {
        }
        return value;
    }

}
