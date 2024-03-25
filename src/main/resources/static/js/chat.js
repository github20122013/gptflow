    function GPTBoot(opts){

        this.domId = 'chat';
        this.role="电商咨询师";
        this.uid = null;
        this.key = null;
        this._conversMp = {};
        this.convers = [];
        this.socket = null;
        this.uuid_str = null;
        this.contexts = ["dept_nm","gdsg_nms_4","brand_nm"];
        this.chat = null;
        this.eventBus = null;
        this.currentConvers = null;
        var _that = this;

        if (opts) {
            for (let fld in opts) {
                if (fld in _that) {
                    _that[fld] = opts[fld];
                }
            }
        }

        this.init = function(opts) {
            if (opts) {
                for (let fld in opts) {
                    if (fld in _that) {
                        _that[fld] = opts[fld];
                    }
                }
            }
            let text = '';
            _that.eventBus = new EventBus();
            _that.chat = document.getElementById(this.domId);
            //let uid = window.localStorage.getItem("uid");
            if (_that.uid == null || _that.uid == '' || _that.uid == 'null') {
                _that.uid = _that.uuid();
            }
            //const messageBox = document.getElementById('message-box');
            const messageBox = document.getElementById(_that.domId);
            const scrollBar = document.getElementById(_that.domId);

            // 监听消息框的scroll事件
            messageBox.addEventListener('scroll', function() {
                // 获取消息框的滚动高度和可见高度
                const scrollTop = messageBox.scrollTop;
                const clientHeight = messageBox.clientHeight;
                // 获取消息框的总高度
                const scrollHeight = messageBox.scrollHeight;
                // 计算阈值，这里设置为可见高度的一半
                const threshold = clientHeight / 2;
                // 判断是否接近底部
                if (scrollHeight - scrollTop - clientHeight <= threshold) {
                    // 将滚动条自动向下滚动到一定高度
                    scrollBar.scrollTop = scrollBar.scrollHeight;
                }
            });
            // 设置本地存储
            window.localStorage.setItem("uid", _that.uid);
            if (typeof (WebSocket) == "undefined") {
                console.log("您的浏览器不支持WebSocket");
            } else {
                console.log("您的浏览器支持WebSocket");
                //实现化WebSocket对象
                //指定要连接的服务器地址与端口建立连接
                //注意ws、wss使用不同的端口。我使用自签名的证书测试，
                //无法使用wss，浏览器打开WebSocket时报错
                //ws对应http、wss对应https。
                var proctocol =  window.location.protocol;
                var socketProctocal = "ws:";
                if("https:"==proctocol){
                    socketProctocal = "wss:";
                }
                _that.socket = new WebSocket(socketProctocal+"//sngpt2c.cnsuning.com/gptadm/chatsocket/"+_that.uid);
                // socket = new WebSocket("ws://mypre.cnsuning.com/gptadm/chatsocket/"+uid);
                //连接打开事件
                _that.socket.onopen = function () {
                    console.log("Socket 已打开");
                };
                //收到消息事件
                _that.socket.onmessage = function (msg) {
                    scrollBar.scrollTop = scrollBar.scrollHeight-60;
                    if (msg.data == "[DONE]") {
                        text = '';
                        _that.currentConvers={};
                        return;
                    }
                    // console.log(msg);
                    let json_data = JSON.parse(msg.data)
                    if (json_data.content == null || json_data.content == 'null') {
                        text = '';
                        return;
                    }
                    text = text + json_data.content;
                    _that.currentConvers.answer=text;
                    //setText(text, uuid_str)
                };
                //连接关闭事件
                _that.socket.onclose = function () {
                    console.log("Socket已关闭:uid="+_that.uid);
                    //setText("连接已经关闭， 请重新登录", _that.uuid_str);

                };
                //发生了错误事件
                _that.socket.onerror = function () {
                    alert("服务异常请重试并联系开发者！")
                }
                //窗口关闭时，关闭连接
                window.unload = function () {
                    _that.socket.close();
                };
            }
            const textarea = document.querySelector('textarea');
        };

        this.destroy = function(){
            if(_that.socket){
               _that.socket.close();
            }
        }

        /**
         * 语义成分标注
         * @param key
         */
        this.doEmbed = function(){
            if(_that.key && _that.contexts){
                var context = _that.contexts[0];
                if(context){
                    var conv = _that.buildConversation(context,0);
                    _that.eventBus.on(context+'_reply',function(){
                        _that.buildNextConversation(conv);
                    });
                    _that.askGPT(conv);
                }
            }
        }

        /**
         * 将对话串成链表，每个context(如 事业部、四级商品组、品牌)一个对话，对话成功，创建下一个对话
         * 由于上一次对话结束，会传标注后的答案给下一个对话，所以要等回复之后，调用搜索接口，减少AGG数量，再去创建第二轮会话，否则的话，问题很长，GPT很耗钱
         * @param conv
         * @returns {null}
         */
        this.buildNextConversation = function(conv){
            var result = null;
            if(conv){
                let idx = conv.idx;
                if(idx>=0){
                    let nextIdx = idx+1;
                    if(nextIdx<=_that.contexts.length-1){
                        let nextContext = _that.contexts[nextIdx];
                        result = _that.buildConversation(nextContext,nextIdx);
                        _that.eventBus.on(nextContext+'_reply',function(){
                                    _that.buildNextConversation(result);
                            }
                        );
                        _that.askGPT(result);
                    }
                }
                // link conversation as linkedlist
                conv.nextConv = result;
            }
            return result;
        }

        this.buildConversation=function (context,i){
            var num = i+1;
            console.log('conv_idx:'+num);
            var result = new Conversation({eventBus:_that.eventBus});
            result.boot = _that;
            if(i==0){
                result.question = "你好,假如你是"+_that.role+"，主营业务是家电3C，顾客搜索"+_that.key+'，我来问你几个问题\u000A';
                result.formal = result.question;
            }
            result.idx = i;
            result.context = context;
            //result.formal = result.formal + num + '.顾客是为了查找以下哪个'+fieldMp[context].title+'的商品;';
            result.formal =  num + '.'+_that.key+'属于哪个'+fieldMp[context].title+'?';
            result.question = result.question + num + '.顾客是为了查找以下哪个'+fieldMp[context].title+'的商品:'+getAggName(context).join(",")+'\r\n'
            //result.formal = result.question;
            result.aggs = getAgg(context);
            return result;
        }

        /**
         * 成功后给业务方回调
         * @param conv
         */
        this.afterReplyFine = function(conv){

        }

        /**
         * 失败后给业务方回调
         * @param conv
         */
        this.afterReplyWrong = function(conv){

        }

        this.setText = function(text, uuid_str) {
            let content = document.getElementById(uuid_str)
            content.innerHTML = marked(text);
        }

        this.uuid = function () {
            var s = [];
            var hexDigits = "0123456789abcdef";
            for (var i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
            s[8] = s[13] = s[18] = s[23] = "-";

            var uuid = s.join("");
            return uuid;
        }

        this.askGPT = function(conv) {
            this.currentConvers = conv;
            if(!this._conversMp[conv.idx]){
                this.convers.push(conv);
                this._conversMp[conv.idx]=conv;
            }
            this.socket.send(conv.question); // 发送消息
        }

        this.askGPTAgain = function(conv){
            if(conv){
                conv.result='';
                conv.question = "(这个答案不太正确，请再考虑下,假如你是电商客服(微笑)"+conv.question;
                this.askGPT(conv);
            }
        }
    }

    function Conversation(opt){
        this.boot = null;
        this.eventBus = opt.eventBus;
        this.formal = '';
        this.question = '';
        this.context = '';
        this.idx = null;
        this.aggs = [];
        this.fineAggs = [];
        this.wrongAggs = [];
        this.answer = '';
        this.result = '';
        this.nextConv = null;
        var _that = this;
        this.reply = function(){
            // 给2秒时间，让业务回调，回调结束后，触发成功时间
            setTimeout(function(){
                _that.eventBus.emit(_that.context+'_reply');
                },
                3000
            );
        };
        var _that = this;
        this.replyFine = function(){
            this.result = 'fine';
            //如果确认结果正确，则将排序值赋值给agg结果
            if(_that.aggs && _that.aggs.length){
                _that.aggs.forEach(function(agg){
                    let txt = agg.key;
                    if(txt){
                        let idx = _that.answer.indexOf(txt);
                        if(idx>0){
                            _that.fineAggs.push(agg);
                        }else{
                            _that.wrongAggs.push(agg);
                        }
                    }
                });
            }
            //机器人与页面交互回调函数
            _that.boot.afterReplyFine(_that);
            _that.reply();
        };
        this.replyWrong = function(){
            this.result = 'wrong';
            _that.boot.afterReplyWrong(_that);
            _that.reply();
        }
    }




