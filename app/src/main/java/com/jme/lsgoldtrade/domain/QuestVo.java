package com.jme.lsgoldtrade.domain;

import java.util.List;

public class QuestVo {

    /**
     * greeting : 您好，请问有什么可以帮您？
     * questionList : [{"id":"1140541574875430914","title":"密码忘记了如何找回？","anwser":"您可以点击忘记密码，进行密码重置。","typeId":4,"createTime":"2019-06-17 16:47:56","isSpread":"0"},{"id":"1140541574867042305","title":"开户需要准备什么资料？","anwser":"开户需要准备身份证和工商银行的储蓄卡，如无工行储蓄卡，开户前将先申请工商银行电子账户。","typeId":1,"createTime":"2019-06-17 16:47:56","jumpTxt":"百度地址","isSpread":"0"},{"id":"1140541574871236609","title":"如何进行撤单操作？","anwser":"进入交易\u2014\u2014撤单界面，选择要撤单的该笔订单进行撤单操作。","typeId":2,"createTime":"2019-06-17 16:47:56","isSpread":"0"},{"id":"1140541574875430913","title":"如何进行入金操作？","anwser":"进入交易界面，点击入金，输入入金金额和验证码即可完成入金。","typeId":3,"createTime":"2019-06-17 16:47:56","isSpread":"0"}]
     */

    private String greeting;
    private List<QuestionListBean> questionList;

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public List<QuestionListBean> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionListBean> questionList) {
        this.questionList = questionList;
    }

    public static class QuestionListBean {
        /**
         * id : 1140541574875430914
         * title : 密码忘记了如何找回？
         * anwser : 您可以点击忘记密码，进行密码重置。
         * typeId : 4.0
         * createTime : 2019-06-17 16:47:56
         * isSpread : 0
         * jumpTxt : 百度地址
         */

        private String id;
        private String title;
        private String anwser;
        private String typeId;
        private String createTime;
        private String isSpread;
        private String jumpTxt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAnwser() {
            return anwser;
        }

        public void setAnwser(String anwser) {
            this.anwser = anwser;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getIsSpread() {
            return isSpread;
        }

        public void setIsSpread(String isSpread) {
            this.isSpread = isSpread;
        }

        public String getJumpTxt() {
            return jumpTxt;
        }

        public void setJumpTxt(String jumpTxt) {
            this.jumpTxt = jumpTxt;
        }
    }
}
