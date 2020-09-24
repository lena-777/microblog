new Vue({
    el:"#app",
    data() {

        //检验密码格式是否正确
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            }else if (value.length<6||value.length>12){
                callback(new Error('密码长度为6-12位字符'));
            }else if (!value.match("^[a-zA-Z0-9_\u4e00-\u9fa5]+$")){
                callback(new Error('请不要输入特殊字符'));
            }
            else {
                if (this.ruleForm.checkPass !== '') {
                    this.$refs.ruleForm.validateField('checkPass');
                }
                callback();
            }
        };
        //检验两次输入的密码是否一致
        var validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.ruleForm.password) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        // 检验邮箱格式是否正确
        var checkEmail = (rule, value, callback) => {
            var reg = /^[a-zA-Z0-9]+@[a-z0-9]{2,5}\.[a-z]{2,3}(\.[a-z]{2,3})?$/;
            if (value === '')
                callback();
            if (!reg.test(value)){
                callback(new Error('邮箱输入格式错误'));
            }
            else{console.log('email pass ...');}
        };

        return {
            ruleForm: {
                username: "",
                sex: "男" ,
                email:'',
                password:"",
                checkPass:"",
                regist:false,

            },
            user:{
                username:"",
                password:"",
                sex:"",
                email:""
            },
            rules: {
                username: [
                    { required: true, message: '请输入昵称', trigger: 'blur' },
                    { min: 2, max: 10, message: '昵称长度在2-10个字符', trigger: 'blur' }
                ],
                sex: [
                    { required: true, message: '请选择你的性别', trigger: 'change' }
                ],
                email:[
                    { validator: checkEmail, trigger: 'blur' }
                ],
                password: [
                    { validator: validatePass, trigger: 'blur' },
                    { required: true, message: '请输入密码', trigger: 'blur' },
                ],
                checkPass: [
                    { required: true, message: '请再次输入密码', trigger: 'blur' },
                    { validator: validatePass2, trigger: 'blur' }
                ],
            },


        };
    },
    methods: {        //注册用户
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    //console.log('success');
                    axios.post(`/users/register`,this.ruleForm).then(response=>{
                        //console.log(response.data);
                        this.user=response.data;
                        window.location.href="index.html?loginId="+this.user.userId;  //页面跳转
                    }).catch(function (error) {
                        console.log(error);
                    });
                } else {
                    console.log('注册失败');
                    return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        }
    }
});