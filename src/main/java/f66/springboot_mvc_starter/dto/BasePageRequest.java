package f66.springboot_mvc_starter.dto;

import lombok.Data;

import java.util.Set;

@Data
public abstract class BasePageRequest {

    protected int page = 0;

    protected int size = 10;

    protected long offset;

    protected String order = "id_desc";

    protected abstract Set<Integer> getAllowedSize();

    protected abstract int getDefaultSize();

    public void calculateOffset() {

        this.offset = (long) page * size;
    }

    public void setSize(int size) {
        if (!getAllowedSize().contains(size)) {

            this.size = getDefaultSize();
        } else {

            this.size = size;
        }
    }
}
