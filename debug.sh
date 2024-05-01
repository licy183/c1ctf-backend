# 注册
curl 127.0.0.1:8080/api/user/register -d '{"username": "12355c","password":"123asdasd", "email":"me@rmb122.cn", "verify_code":"jlMv7J","student_id":"2017212183","student_name":"123","is_student":false}' -H "Content-Type: application/json"

# 发送注册邮件
curl 127.0.0.1:8080/api/user/send_verify_mail -d '{"email":"me@rmb122.cn"}' -H "Content-Type: application/json"

# 登录
curl 127.0.0.1:8080/api/user/login -d '{"username": "12355c","password":"123asdasd"}' -H "Content-Type: application/json"

# 忘记密码
curl 127.0.0.1:8080/api/user/forget_password -H "Content-Type: application/json" -d '{"username": "12355c","new_password":"123asdasd", "verify_code": ""}'
