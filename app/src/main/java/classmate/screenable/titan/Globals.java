package classmate.screenable.titan;

import android.os.Environment;

import java.io.File;

/**
 * Created by Wise on 10/26/2017.
 */

public class Globals {

//    youtube
    protected static String prefix="http://classmateapi.screenableinc.com";
//    protected static String prefix="http://192.168.10.105:3000";
    protected static String api_search_url = "https://www.googleapis.com/youtube/v3/search?";
    protected static String api_slideshare_url = "https://www.slideshare.net/api/2/";
    protected static String api_classmate_url=prefix+"/setup?";
    protected static String api_error_report=prefix+"/reportError?";
    protected static String youtube_video_watch = "https://www.youtube.com/watch?v=";
    protected static String folder = Environment.getExternalStorageDirectory() + File.separator + "classmate";
    protected static String profile_folder = Environment.getExternalStorageDirectory() + File.separator + "classmate"+File.separator+"profile";
    protected static String unilus_folder = Environment.getExternalStorageDirectory() + File.separator + "classmate"+File.separator+"unilus";
    protected static String slideshare_folder = Environment.getExternalStorageDirectory() + File.separator + "classmate"+ File.separator+"slideshare";
    protected static String question_bank_url = "https://sms.unilus.ac.zm/Students/QuestionBank.aspx";

    protected static final String theme_lawrencium = "lawrencium";
    protected static final String host = "https://www.unilus.ac.zm";
    protected static final String portal = "https://sms.unilus.ac.zm/Students/StudentPortal.aspx";
    protected static final String material_prefix = "https://sms.unilus.ac.zm";
    protected static final String portal_pic ="https://sms.unilus.ac.zm/UserImages/";

    protected static final String id_keyName = "student_id";
    protected static final String CATEGORY_VID_SHAREDPREF_KEY_NAME = "videos";
    protected static final String SETUP_COMPLETE_KEY_NAME = "setup_complete";
    protected static final String CATEGORY_SLIDE_SHAREDPREF_KEY_NAME = "slides";
    protected static final String passwordKeyName = "password";
    protected static final String registered_courses_url = "https://sms.unilus.ac.zm/Students/Registration.aspx";
    protected static final String materials_url = "https://sms.unilus.ac.zm/Students/Materials.aspx";
    protected static final String registration_url = "https://sms.unilus.ac.zm/Students/RegistrationForm.aspx";
    protected static final String lectscon_url = "https://sms.unilus.ac.zm/Students/LecturerContact.aspx";
    protected static final String login_url = "https://sms.unilus.ac.zm/Students/Login.aspx";
    protected static final String viewca_url = "https://sms.unilus.ac.zm/Students/ViewCA.aspx";
    protected static final String viewfinal_url = "https://sms.unilus.ac.zm/Students/ViewResults.aspx";
    protected static final String assignments_url = "https://sms.unilus.ac.zm/Students/Assignments.aspx";
    protected static final String APP_VERSION_NUMBER = "1.0";
    protected static final String DEBUG_TAG="DEBUGTITAN";
    protected static final String ERROR="ERROR";
    protected static final String AMBIG_ERROR_MESSAGE="SOMETHING WENT WRONG";
    protected static final String SUCCESS_MESSAGE="SUCCESS";
//    protected static final String SUCCESS_MESSAGE="SUCCESS";



}
