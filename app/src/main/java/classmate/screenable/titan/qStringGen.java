package classmate.screenable.titan;

import java.util.List;

public class qStringGen {
    public static String qString(List<String> paramList){


            StringBuilder result = new StringBuilder();
            for (String param : paramList) {
                if (result.length() == 0) {
                    result.append(param);
                } else {
                    result.append("&" + param);
                }
            }
            return result.toString();


    }
}
