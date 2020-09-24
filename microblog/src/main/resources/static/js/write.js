new Vue({
   el:"#app",
   data() {
       return {
           passage:{        //当前文章
               userId:0,
               passageId:0, //文章id
               title:'',
               described:'',
               content:'',
               isPrivate:'',
               originalPassageId:0, //转载原文的Id
               isPublish:0, //默认存在草稿箱。当点击发表的时候，默认修改为1。若点击了关闭，则默认存在草稿箱
           },
       }
   },
    methods:{
       //提交文章
        submitPassage:function () {
            axios.post(`/passage/insert?passage=${this.passage}`).then(response=>{
                if (response.data.msg==100){        //当msg返回状态100表示成功
                    alert("发表文章成功！");
                } else{
                    alert("发表文章失败。");
                }
            })
        },
        //点击提交按钮后 修改isPublish并提交文章
        changeAndSubmit:function () {
            this.isPublish=1;   //修改isPublish
            if (this.passage.passageId==0) {
                this.submitPassage();    //提交
            }
            else{       //修改文章
                this.updatePassgae();
            }
        },
        //修改文章
        updatePassgae:function () {
            axios.post(`/passage/update?passage=${this.passage}`).then(response=>{
                if (response.data.msg==100){        //当msg返回状态100表示成功
                    alert("更新文章成功！");
                } else{
                    alert("更新文章失败。");
                }
            })
        }
    },
    create:function (loginUser,passage) {
        this.passage.userId=loginUser.userId;
        if (passage!=null){
            this.passage=passage;
        }
    }
});