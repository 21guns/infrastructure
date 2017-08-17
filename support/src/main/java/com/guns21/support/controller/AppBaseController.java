package com.guns21.support.controller;

public abstract class AppBaseController extends BaseController {
//    @Autowired
//    protected MessageSource messages;
//    private static Logger logger = LoggerFactory.getLogger(AppBaseController.class);
//
//    /**
//     * 错误信息描述：错误码|错误描述
//     * @param bindingResult
//     * @return
//     */
//    protected Result validation(BindingResult bindingResult) {
//        return validation(bindingResult.getFieldErrors());
//    }
//
//    /**
//     * 错误信息描述：错误码|错误描述
//     * @return
//     */
//    protected Result validation(List<FieldError> fieldErrors) {
//        for (FieldError fieldError : fieldErrors) {
//            String message = fieldError.getDefaultMessage();
//            logger.warn(message + "[" + Arrays.toString(fieldError.getArguments()) + "]");
//
//            if (!message.contains("|")) {
//                return Result.fail(message);
//            }
//            String[] split = StringUtils.split(message, "|");
//            if (split.length == 2) {
//                return Result.fail(split[1],split[0]);
//            } else {
//                return Result.fail(split[0]);
//            }
//        }
//
//        return Result.success();
//    }


}
