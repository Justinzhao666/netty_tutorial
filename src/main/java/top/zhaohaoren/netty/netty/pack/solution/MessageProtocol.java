package top.zhaohaoren.netty.netty.pack.solution;

/**
 * 自定义协议-协议包结构
 *
 * @author zhaohaoren
 */
public class MessageProtocol {
    private int len;
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
