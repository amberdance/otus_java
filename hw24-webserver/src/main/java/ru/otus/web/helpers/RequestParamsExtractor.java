package ru.otus.web.helpers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestParamsExtractor {

    public static final int ID_PATH_PARAM_POSITION = 1;

    public static long extractIdFromRequest(HttpServletRequest request) {
        var paths = request.getPathInfo().split("/");
        var id = (paths.length > 1) ? paths[ID_PATH_PARAM_POSITION] :
                String.valueOf(-1);

        return Long.parseLong(id);
    }
}
