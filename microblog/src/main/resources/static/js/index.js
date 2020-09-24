new Vue({
    el:"#app",
    data(){
        return {
            keywordData:[],     //前十的关键词
            current:1,  //存储当前页
            size:12,    //存储当前页数据条数
            total:10,      //存储当前总数据条数
            passageData:[],     //当前页文章
            keyword:[],      //当前选择的关键词id
            login:false,   //默认未登录
            user:[],      //存放当前登录用户信息
            inputSearchPassages:'',     //输入框填写的内容
            userData:[],    //存放排名高的用户
        }
    },
    methods:{

        //t初始化登录的用户
        InitLogin:function(){
            QueryString.Init(); //初始化
            var id=QueryString.GetValue("loginId");
            if (id === ''){         //没有登录的用户
                this.login=false;
            }
            else{
                //查询用户
                this.findUserById(id);
                this.login=true;
            }
        },
        //t根据用户ID查询当前登录用户
        findUserById:function(id){
            axios.get(`/users/findOne?userId=`+id).then( response => {
                this.user=response.data;
            }).catch(function (error) {
                console.log(error);
            });
        },
        //t查询关注度高的用户
        findTopUser:function () {
            //alert("icome")
            axios.get(`/users/findTop`).then( response => {
                this.userData=response.data;
            }).catch(function (error) {
                console.log(error);
            });
        },
        //t根据输入的关键词 模糊查询文章 其中：inputSearchPassages存放了关键词
        searchPassage:function () {
           // alert(this.inputSearchPassages);
            if(this.inputSearchPassages==""){
                this.findAllPassage();  //查询所有文章
            }
            else{
                this.current=1;
                //查询文章
                axios.get(`/passage/findByKeyword?name=${this.inputSearchPassages}&current=${this.current}&size=${this.size}`)
                    .then(response=>{
                        this.passageData=response.data;         //文章数据变成查询的相关文章
                    }).catch(function (error) {
                        console.log(error);
                    });
            }
        },
        //t退出登录
        loginout:function () {
            this.login=false;   //退出登录
            this.user=[];     //清空登录用户数据
           // this.reload();  //刷新页面
        },
        //t页面返回初始状态
        returnIndex:function () {
            this.current=1;         //返回第一页
           // this.size=12;       //初始设定是每页12条数据
            this.findAllPassage();
        },
        //t根据点击的关键词查询文章
        findPassage:function (keyword){
            axios.get(`/keyword/findPassageByKeywordId?keywordId=${keyword.keywordId}&current=${this.current}&size=${this.size}`)
                .then(response=>{
                this.keyword=keyword;       //将当前文章的关键词复制给keyword
                this.passageData=response.data.records;      //分页结果集
                this.total=response.data.total;     //总记录数
            }).catch(function (error) {
                    console.log(error);
                }).catch(function (error) {
                    console.log(error);
                });
        },
        //t查询关键词热度前十
        findTopKeyword:function(){
            //var _this=this;
            axios.get(`/keyword/findTop`).then(response=>{
                this.keywordData=response.data;
            }).catch(function (error) {
                console.log(error);
            });
        },
        //t查询热度较高的文章
        findAllPassage:function () {
            //var _this=this;
            axios.get(`/passage/findByPage?current=${this.current}&size=${this.size}`).then(response=>{
                this.passageData=response.data.records;      //分页结果集
                this.total=response.data.total;     //总记录数
            }).catch(function (error) {
                console.log(error);
            });
        }
    },
    created:function () {       //启动是调用该方法
        this.findTopKeyword();     //查询关键词
        this.findAllPassage();       //查询文章
        this.findTopUser();     //查找关注度高的用户
        this.InitLogin();       //初始化登录用户
    }
});