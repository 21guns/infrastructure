# 鉴权模块
  permit:
    #pages: /cust/v1/sms/send/code
    matcher: regex
    pages: /cust/(?!v1/users/login|v1/users/logout|v1/leads/appoint|v1/leads/appoint/myAppoint)(.*)
# login 和 logout不能包含在permit中，需要授权的也不能包含在其中

# 对于资源路径需要遵循AntPath协议