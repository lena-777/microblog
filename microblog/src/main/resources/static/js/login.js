new Vue({
    el:"#app",
   // router:new VueRouter();
    data(){
        return {
            user:[{             //登录用户的输入框信息
                id:'',
                password:''
            }],
            u:[],   //若登录成功，则为当前登录用户信息；若不成功，则为空。
            pass:false,
        }

    },
    methods:{
        //用户登录
        submit:function () {
            axios.get(`/users/login?userId=${this.user.id}&password=${this.user.password}`).then(response=>{
                //若登录成功，则u为当前登录用户信息；若不成功，则u为空。
                //alert(this.u);
                if (response.data==null){
                    //进行弹框提示?4
                    alert("账号密码不正确，请重新输入");
                }
                else{
                    this.u=response.data;
                    //跳转到index.html
                    window.location.href="index.html?loginId="+this.user.id;
                }
            }).catch(function (error) {
                console.log(error);
            });
        }

    },
    created:function () {


    }
});