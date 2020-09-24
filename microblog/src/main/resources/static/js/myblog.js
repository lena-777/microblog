new Vue({
    el:"#app",
    data() {
        return {
            user:{},        //存放用户个人信息
            passageData:[],      //存放该用户的文章
            fans:[],    //当前用户的粉丝详情
            attentions:[],      //存放当前用户关注人的信息
            isMe:false, //判断是否是用户查看自己的博客，默认是false
            loginUser: {} ,            //登录用户
            collectionPassages:[],      //用户收藏的文章
            current:1,      //当前页
            size:12,        //当前文章条数
            total:10,       //当前文章总数目
            searchId:0,     //查询用户的ID       (如果直接使用this.user.userId获取不到数据，会导致传参null获取不到文章)
            catagoryData:[],    //存放分类数据

        }
    },
    methods:{
        //t初始化登录和查询用户、用户文章
        InitData:function(){
            QueryString.Init();     //初始化
            var loginId=QueryString.GetValue("loginId");
            var searchId=QueryString.GetValue("searchId");
            this.searchId=searchId;
            if (loginId == searchId){
                this.isMe=true;
                this.findUserById(searchId);//根据用户Id查询用户信息
           //     console.log("isMe"+this.user);
                this.loginUser=this.user;
            }
            else{
                this.findMeById(loginId);   //根据用户ID查询当前登录用户
                this.findUserById(searchId);//根据用户Id查询用户信息
            }
            this.findPassageById();   //根据用户Id查询用户编写的文章
            this.findCatagoryById();    //查询用户分类的数据

        },
        //查询用户分类的数据
        findCatagoryById:function () {
            console.log(this.searchId);
            axios.get(`/catagory/findAll?userId=${this.searchId}`).then(response=>{
                this.catagoryData=response.data;
                console.log("用户分类初始化成功......")
            })
        },
        //查询用户收藏的文章
        findCollection:function(){
            axios.get(`/passage/findCollectionByUserId?userId=${this.user.userId}&current=${this.current}&size=${this.size}`)
                .then(response=>{
                    this.collectionPassages=response.data;
                })
        },
        //t根据用户Id查询用户信息
        findUserById:function(id) {
            axios.get(`/users/findOne?userId=${id}`).then( response=>{
                this.user=response.data;
                console.log("当前查询用户:"+this.user.username);
            }).catch(function (error) {
                console.log(error);
            });
        },
        //t根据用户ID查询当前登录用户
        findMeById:function(id){
            axios.get(`/users/findOne?userId=${id}`).then( response => {
                this.loginUser=response.data;
                console.log("当前登录用户:"+this.loginUser.username);
            }).catch(function (error) {
                console.log(error);
            });
        },
        //t根据用户Id查询用户编写的文章
        findPassageById:function () {
            //alert(this.searchId);
            axios.get(`/passage/findByUserId?userId=${this.searchId}&current=${this.current}&size=${this.size}`).then(response=>{
                this.passageData=response.data;
              // console.log("文章"+this.passageData);
            }).catch(function (error) {
                console.log(error);
            });
        },
        //当点击粉丝数目的时候，查询粉丝详情
        findFansById:function () {
            axios.get(`/users/findFans?userId=${this.user.id}`).then(response=>{
                this.fans=response.data;
            })
        },
        //当点击关注人数目的时候，触发查询关注人详情
        findAttById:function () {
            axios.get(`/users/findAttention?userId=${this.user.id}`).then(response=>{
                this.attentions=response.data;
            })
        },
        //关注
        attentionUser:function () {
            axios.get(`/users/attention?attId=${this.loginId}&beAttId=${this.user.id}`).then(response=>{
                if (response.data.msg==100){        //当msg返回状态100表示成功
                    alert("关注成功！");
                } else{
                    alert("关注失败。");
                }
            })
        },
        //取消关注
        cancelAttention:function(){
            axios.get(`/users/cancelAttention?attId=${this.loginId}&beAttId=${this.user.id}`).then(response=>{
                if (response.data==1){
                    alert("取消关注成功！");
                } else{
                    alert("取消关注失败。");
                }
            })
        },
    },
    created:function () {      //其中loginId是当前登录用户的id，id是查询用户的id
        this.InitData();
    }

});