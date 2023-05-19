package com.juzi.jubi.manager;

import com.juzi.jubi.config.ModelConfigProperties;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 对接AI接口
 *
 * @author codejuzi
 */
@Service
public class AiManager {

    @Resource
    private YuCongMingClient yuCongMingClient;

    @Resource
    private ModelConfigProperties modelConfigProperties;


    /**
     * 对接鱼聪明AI接口
     *
     * @param message 用户输入内容
     * @return AI生成的内容
     */
    public String doGenByYuCongMing(String message) {
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelConfigProperties.getModelId());
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
        return response.getData().getContent();
    }
}
