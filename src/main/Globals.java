package main;

import config.ConfigData;

import java.awt.Dimension;
import java.awt.Font;

public class Globals
{
	public static final String APPLICATION_TITLE = "Tilt Notes";
	public static final String APPLICATION_VERSION = "v2.2.0";
	public static final String APPLICATION_AUTHOR = "Steve Tilt";
	public static final String APPLICATION_PUBLISHER = "Tilt Technologies, Inc.";
	public static final String APPLICATION_COPYRIGHT = "Released into the public domain by Tilt Technologies, Inc., 2013.";
	public static final String APPLICATION_TRADEMARK = "Steve Tilt, Tilt Technologies and the picture of the smirking cat are trademark of John McCullock.";
	public static final String APPLICATION_SUPPORT = "Any questions, comments, rants or haphazardly constructed utterances that achieve some level of coherence will generally be ignored unless Steve is properly supplied with fish treats or tummy rubs.  Such contributions are not guaranteed a response, but it will certainly help your chances.";
	public static final String APPLICATION_THANKS = "Special thanks to bad blood (Annassi Mehdi) of deviantart.com for creating these awesome emoticons. See also DIST Yolks Emoticons at dafont.com";
	public static final String APPLICATION_PUBLIC_DOMAIN_NOTICE = "Tilt Notes binaries, source code, and any files accompanying a Tilt Notes distribution are released into the public domain as described in the Unlicense copyright waiver \"UNLICENSE.TXT\" originally distributed with this Tilt Notes package.  The author and Tilt Technologies, Inc. have voluntarily opted out of the copyright industry's game altogether and set this code free.  This software has been donated to the public domain \"as is\", with no warranty.";
	public static final String TILT_TECH_LOGO_64x51 = "/images/tilt_icon_64x51.png";
	public static final String TILT_TECH_LOGO_32x25 = "/images/tilt_icon_32x25.png";
	public static final String TILT_TECH_LOGO_16x13 = "/images/tilt_icon_16x13.png";
	public static final String LARGE_LOGO_PATH = "/images/steve1.jpg";
	public static final String PUBLIC_DOMAIN_LOGO = "/images/public_domain_logo_40x40.png";
	public static final String ABOUT_WINDOW_TITLE = "About...";
	public static final int ABOUT_WINDOW_WIDTH = 640;
	public static final int ABOUT_WINDOW_HEIGHT = 480;
	public static final String APPLICATION_ICON_16x16 = "/images/bell2_16x16.png";
	public static final String APPLICATION_ICON_24x24 = "/images/bell2_24x24.png";
	public static final String APPLICATION_ICON_32x32 = "/images/bell2_32x32.png";
	public static final String APPLICATION_ICON_48x48 = "/images/bell2_48x48.png";
	
	public static final String CONFIG_FILE_PATH = "config.xml";
	public static final String EMOTICONS_PATH = "/emoticons/";
	public static final String[] EMOTICONS_LIST = new String[]{"/emoticons/0_38x36.png",
		"/emoticons/3_38x36.png",
		"/emoticons/4_38x36.png",
		"/emoticons/5_37x37.png",
		"/emoticons/6_41x37.png",
		"/emoticons/7_38x36.png",
		"/emoticons/8_45x39.png",
		"/emoticons/9_38x37.png",
		"/emoticons/a_47x42.png",
		"/emoticons/b_38x36.png",
		"/emoticons/colon_41x39.png",
		"/emoticons/c_37x36.png",
		"/emoticons/d_38x36.png",
		"/emoticons/e_37x37.png",
		"/emoticons/f_42x36.png",
		"/emoticons/g_39x38.png",
		"/emoticons/hand_36x40.png",
		"/emoticons/h_43x36.png",
		"/emoticons/i_48x37.png",
		"/emoticons/j_39x37.png",
		"/emoticons/k_39x38.png",
		"/emoticons/lbracket_38x37.png",
		"/emoticons/l_41x40.png",
		"/emoticons/m_37x37.png",
		"/emoticons/n_39x39.png",
		"/emoticons/okay_34x41.png",
		"/emoticons/o_39x39.png",
		"/emoticons/peace_21x40.png",
		"/emoticons/point-left_39x19.png",
		"/emoticons/point-right_39x19.png",
		"/emoticons/p_37x36.png",
		"/emoticons/quote_38x36.png",
		"/emoticons/q_38x38.png",
		"/emoticons/rbracket_37x37.png",
		"/emoticons/r_38x36.png",
		"/emoticons/s_38x36.png",
		"/emoticons/thumbs-down_25x31.png",
		"/emoticons/thumbs-up_25x31.png",
		"/emoticons/t_37x41.png",
		"/emoticons/u_37x36.png",
		"/emoticons/v_38x39.png",
		"/emoticons/w_38x36.png",
		"/emoticons/x_37x38.png",
		"/emoticons/y_37x36.png",
		"/emoticons/z_38x38.png",};
	public static final String TEMPLATES_PATH = ".";
	
	public static final String CUT_ICON_PATH = "/images/ed_cut.gif";
	public static final String COPY_ICON_PATH = "/images/ed_copy.gif";
	public static final String PASTE_ICON_PATH = "/images/ed_paste.gif";
	public static final String BOLD_ICON_PATH = "/images/ed_format_bold.gif";
	public static final String ITALIC_ICON_PATH = "/images/ed_format_italic.gif";
	public static final String UNDERLINE_ICON_PATH = "/images/ed_format_underline.gif";
	public static final String STRIKE_ICON_PATH = "/images/ed_format_strike.gif";
	public static final String BACKGROUND_COLOR_ICON_PATH = "/images/ed_color_bg.gif";
	public static final String FOREGROUND_COLOR_ICON_PATH = "/images/ed_color_fg.gif";
	public static final String EMOTICON_ICON_PATH = "/images/emoticon_18x18.png";
	public static final String EMAIL_SEND_ICON_PATH = "/images/email_go.png";
	public static final String CHECKMARK_ICON_PATH = "/images/checkmark_18x18.png";
	
	public static final String[] WEB_SAFE_FONTS = new String[]{"Arial",
																"Arial Black",
																"Calibri",
																"Calisto MT",
																"Cambria",
																"Century Gothic",
																"Comic Sans MS",
																"Courier New",
																"Georgia",
																"Goudy Old Style",
																"Impact",
																"Palatino Linotype",
																"Rockwell",
																"Tahoma",
																"Times New Roman",
																"Trebuchet MS",
																"Verdana"};
	public static final Integer[] SAFE_FONT_SIZES = new Integer[]{8, 9, 10, 11, 12, 
																	14, 16, 18, 20, 22, 
																	24, 26, 28, 36, 48, 72};
	
	public static final boolean UNCHOSEN_USE_BORDER = true;
	public static final boolean UNCHOSEN_USE_TIME_TITLE = true;
	public static final String UNCHOSEN_CUSTOM_TITLE = Globals.APPLICATION_TITLE + " " + Globals.APPLICATION_VERSION;
	public static final ConfigData.Align UNCHOSEN_TITLE_ALIGNMENT = ConfigData.Align.CENTER;
	
	public static final int UNCHOSEN_NOTE_COLOR_RED = 255;
	public static final int UNCHOSEN_NOTE_COLOR_GREEN = 255;
	public static final int UNCHOSEN_NOTE_COLOR_BLUE = 128;
	public static final int UNCHOSEN_TITLE_COLOR_RED = 255;
	public static final int UNCHOSEN_TITLE_COLOR_GREEN = 255;
	public static final int UNCHOSEN_TITLE_COLOR_BLUE = 128;
	public static final int UNCHOSEN_BORDER_COLOR_RED = 255;
	public static final int UNCHOSEN_BORDER_COLOR_GREEN = 255;
	public static final int UNCHOSEN_BORDER_COLOR_BLUE = 128;
	public static final int UNCHOSEN_BORDER_WIDTH = 4;
	
	public static final String UNCHOSEN_NOTE_FONT_FACE = "Arial";
	public static final int UNCHOSEN_NOTE_FONT_SIZE = 12;
	public static final boolean UNCHOSEN_NOTE_FONT_BOLD = false;
	public static final boolean UNCHOSEN_NOTE_FONT_ITALIC = false;
	public static final boolean UNCHOSEN_NOTE_FONT_UNDERLINE = false;
	public static final boolean UNCHOSEN_NOTE_FONT_STRIKEOUT = false;
	public static final String UNCHOSEN_TITLE_FONT_FACE = "Arial";
	public static final int UNCHOSEN_TITLE_FONT_SIZE = 10;
	public static final boolean UNCHOSEN_TITLE_FONT_BOLD = false;
	public static final boolean UNCHOSEN_TITLE_FONT_ITALIC = false;
	
	public static final int UNCHOSEN_NOTE_FONT_COLOR_RED = 0;
	public static final int UNCHOSEN_NOTE_FONT_COLOR_GREEN = 0;
	public static final int UNCHOSEN_NOTE_FONT_COLOR_BLUE = 0;
	public static final int UNCHOSEN_TITLE_FONT_COLOR_RED = 128;
	public static final int UNCHOSEN_TITLE_FONT_COLOR_GREEN = 128;
	public static final int UNCHOSEN_TITLE_FONT_COLOR_BLUE = 128;
	
	public static final int UNCHOSEN_NOTE_WIDTH = 350;
	public static final int UNCHOSEN_NOTE_HEIGHT = 250;
	
	public static final String UNCHOSEN_MULTICAST_IP = "224.23.0.7";
	public static final int UNCHOSEN_MULTICAST_PORT = 2307;
	public static final int UNCHOSEN_CENSUS_INTERVAL = 2000;
	public static final int BUFFER_SIZE = 65536;
	
	public static final boolean UNCHOSEN_CONFIRM_BEFORE_DELETE = false;
	public static final boolean UNCHOSEN_SHOW_TEMPLATE_ITEM_IN_TRAY_MENU = false;
	public static final boolean UNCHOSEN_SHOW_SPLASH_AT_STARTUP = true;
	public static final boolean UNCHOSEN_PLAY_SOUND_ON_RECEIVING = false;
	public static final boolean UNCHOSEN_NOTES_EXPIRE = true;
	public static final int UNCHOSEN_EXPIRATION_MINUTES = 15;
	
	public static final int SESSION_TIMER_INTERVAL = 5000;
	
	public static final String CONFIGURATION_GUI_TITLE = "Settings";
	public static final Dimension CONFIGURATION_GUI_MINIMUM_SIZE = new Dimension(500, 600);
	public static final String MULTICAST_ADDRESS_NOTES = "Addresses in the range of 239.0.0.0 to 239.255.255.255 are reserved for administratively scoped addresses. Administratively scoped addresses as defined by RFC 2365 are used to prevent the forwarding of multicast traffic across boundaries configured for the address.\n\nAddresses in the range of 224.0.1.0 to 238.255.255.255 are known as globally scoped addresses, which means they can be used for multicasting across an intranet or the Internet. Some addresses within this range are reserved by the Internet Assigned Numbers Authority (IANA) for special purposes.";
	
	public static final Dimension CENSUS_GUI_MINIMUM_SIZE = new Dimension(275, 350);
	public static final int CENSUS_GUI_DATA_COLLECTION_TIME_LIMIT = 5000;
	public static final int CENSUS_TABLE_REFRESH_INTERVAL = 1000; // in milliseconds.
	public static final int CENSUS_TABLE_MAX_AGE = 10; // roughly 10 seconds.
	public static final Font CENSUS_TABLE_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
	public static final int CENSUS_TABLE_ROW_HEIGHT = 20;
	
	public static final String HISTORY_GUI_TITLE = "TiltNotes History";
	public static final Dimension HISTORY_GUI_MINIMUM_SIZE = new Dimension(400, 300);
	public static final Dimension HISTORY_GUI_PREFERRED_SIZE = new Dimension(800, 600);
	public static final int HISTORY_EXPIRATION = 60000; // roughly one hour in milliseconds.
	public static final String[] HISTORY_TABLE_COLUMN_NAMES = new String[]{"Deleted At", "Source", "Message", "Created", "Hidden At", "Deletion Command", "Deletion Source"};
}
