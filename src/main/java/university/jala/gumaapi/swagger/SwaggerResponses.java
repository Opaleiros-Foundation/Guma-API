package university.jala.gumaapi.swagger;

import lombok.Data;

@Data
public class SwaggerResponses {

    public static final String CODE_201 = "201";
    public static final String CODE_200 = "200";
    public static final String CODE_204 = "204";
    public static final String CODE_409 = "409";
    public static final String CODE_403 = "403";
    public static final String CODE_404 = "404";
    public static final String CODE_400 = "400";


    public static final String RESPONSE_201 = "201 Successful Registration";
    public static final String RESPONSE_200 = "200 Successful Request";
    public static final String RESPONSE_204 = "204 Successful Request";

    public static final String RESPONSE_409 = "409 User Email Already Exists";
    public static final String RESPONSE_403 = "403 Forbidden";
    public static final String RESPONSE_404 = "404 Not Found";
    public static final String RESPONSE_400 = "400 Bad Request";


}