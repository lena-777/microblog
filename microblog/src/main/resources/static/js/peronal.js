new Vue({
   el:"#app",
   data(){
       return {
           loginId:0,
           user:[] ,    //当前用户信息。
           updateSign:false,    //标志当前界面（可以用来标志前端表单的显示的标志）
       }
   },
    methods:{
       //点击修改按钮后，更改updateSign
        updateSignChange:function(){
            this.updateSign=true;
            //看看是否会自动更新页面？
        },
       //修改后提交个人信息
        update:function (u) {
            axios.get(`/user/update?u=${u}`).then(response=>{
                if (response.data==null) {
                    //当返回users为空时，表示没有修改成功
                    alert("修改失败！");
                }else {
                    this.user=response.data;
                    this.findUserById();     //重新查询用户更新后的信息。
                    this.updateSign=false;   //表示当前修改完成
                }
            })
        },
        //查询用户个人信息
        findUserById:function () {
            axios.get(`/users/findOne?userId=${user.userId}`).then(response=>{
                this.user=response.data;
            });
            this.updateSign=false;
        }
    },
    create:function (user) {
        this.user=user;
    }
});