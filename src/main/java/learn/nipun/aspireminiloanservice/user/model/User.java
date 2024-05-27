package learn.nipun.aspireminiloanservice.user.model;

import java.time.OffsetDateTime;
import lombok.Getter;
import org.hibernate.usertype.UserType;


@Getter
public class User {

    private String userName;
    private String name;
    private Address address;
    private String phoneNumber;
    private String email;
    private UserStatus userStatus;
    private Integer cibilScore;
    private final OffsetDateTime createdOn;
    private OffsetDateTime updatedOn;
    private UserType userType;

    public User(OffsetDateTime createdOn, String userName, String name, Address address, String phoneNumber,
            String email,
            UserStatus userStatus, Integer cibilScore, OffsetDateTime updatedOn, UserType userType) {

        this.createdOn = createdOn;
        this.userName = userName;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.userStatus = userStatus;
        this.cibilScore = cibilScore;
        this.updatedOn = updatedOn;
        this.userType = userType;
    }
}

