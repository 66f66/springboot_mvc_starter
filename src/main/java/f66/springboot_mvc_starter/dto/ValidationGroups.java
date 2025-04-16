package f66.springboot_mvc_starter.dto;

public interface ValidationGroups {
    interface Create {
    }

    interface Update {
    }

    interface Common extends Create, Update {
    }
}
