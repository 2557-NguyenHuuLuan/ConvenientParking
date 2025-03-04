package com.example.convenientparking.Config;

import com.example.convenientparking.Constants.RentalFormEnum;
import com.example.convenientparking.Constants.RoleEnum;
import com.example.convenientparking.Constants.ZoneEnum;
import com.example.convenientparking.Entities.ParkingZone;
import com.example.convenientparking.Entities.RentalForm;
import com.example.convenientparking.Entities.Role;
import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Repositories.ParkingZoneRepository;
import com.example.convenientparking.Repositories.RentalFormRepository;
import com.example.convenientparking.Repositories.RoleRepository;
import com.example.convenientparking.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                        RoleRepository roleRepository,
                                        RentalFormRepository rentalFormRepository,
                                        ParkingZoneRepository parkingZoneRepository){
        return args -> {
            if(roleRepository.findByName(RoleEnum.ADMIN.name()).isEmpty()){
                Role role = Role.builder()
                        .name(RoleEnum.ADMIN.name())
                        .build();
                roleRepository.save(role);
            }
            if(roleRepository.findByName(RoleEnum.EMPLOYEE.name()).isEmpty()){
                Role role = Role.builder()
                        .name(RoleEnum.EMPLOYEE.name())
                        .build();
                roleRepository.save(role);
            }
            if(roleRepository.findByName(RoleEnum.USER.name()).isEmpty()){
                Role role = Role.builder()
                        .name(RoleEnum.USER.name())
                        .build();
                roleRepository.save(role);
            }
            if(userRepository.findByUsername("admin").isEmpty()) {
                Role adminRole = roleRepository.findById(roleRepository.findRoleIdByName(RoleEnum.ADMIN.name()).get()).get();
                Role employeeRole = roleRepository.findById(roleRepository.findRoleIdByName(RoleEnum.EMPLOYEE.name()).get()).get();
                Role userRole = roleRepository.findById(roleRepository.findRoleIdByName(RoleEnum.USER.name()).get()).get();
                Set<Role> roles = new HashSet<>(Arrays.asList(adminRole, employeeRole, userRole));
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("123"))
                        .email("admin@gmail.com")
                        .phone("0947626009")
                        .balance(0L)
                        .address("Ho Chi Minh City University of Technology.")
                        .createdAt(LocalDate.now().atStartOfDay())
                        .isLocked(false)
                        .isVerified(true)
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("admin được khởi tạo với mật khẩu là: 123");
            }
            if(rentalFormRepository.findByForm(RentalFormEnum.HOUR.name()).isEmpty()){
                RentalForm rentalForm = RentalForm.builder()
                        .form(RentalFormEnum.HOUR.name())
                        .pricePer(8000L)
                        .description("Hình thức thuê ô đậu theo giờ phù hợp với những khách hàng cần đậu xe trong thời gian ngắn, ví dụ như khi đi công việc, mua sắm hay tham dự các sự kiện. Khách hàng chỉ cần trả phí cho số giờ sử dụng thực tế, giúp tiết kiệm chi phí nếu chỉ cần đậu trong vài giờ đồng hồ.")
                        .build();
                rentalFormRepository.save(rentalForm);
            }
            if(rentalFormRepository.findByForm(RentalFormEnum.DAY.name()).isEmpty()){
                RentalForm rentalForm = RentalForm.builder()
                        .form(RentalFormEnum.DAY.name())
                        .pricePer(80000L)
                        .description("Hình thức thuê theo ngày phù hợp cho những khách hàng có nhu cầu đậu xe trong cả ngày dài, chẳng hạn như khi đi công tác hoặc tham quan một khu vực trong suốt cả ngày. Phí thuê được tính theo ngày, giúp khách hàng dễ dàng quản lý chi phí trong suốt thời gian đậu xe.")
                        .build();
                rentalFormRepository.save(rentalForm);
            }
            if(rentalFormRepository.findByForm(RentalFormEnum.MONTH.name()).isEmpty()){
                RentalForm rentalForm = RentalForm.builder()
                        .form(RentalFormEnum.MONTH.name())
                        .pricePer(800000L)
                        .description("Hình thức thuê theo tháng dành cho những khách hàng có nhu cầu đậu xe dài hạn, chẳng hạn như dân văn phòng, cư dân khu vực hoặc những người thường xuyên sử dụng dịch vụ đậu xe mỗi ngày. Phí thuê theo tháng mang lại giá trị tiết kiệm cho khách hàng vì họ chỉ phải trả một lần cho cả tháng mà không lo lắng về việc thanh toán từng ngày. ")
                        .build();
                rentalFormRepository.save(rentalForm);
            }
            if(rentalFormRepository.findByForm(RentalFormEnum.YEAR.name()).isEmpty()){
                RentalForm rentalForm = RentalForm.builder()
                        .form(RentalFormEnum.YEAR.name())
                        .pricePer(8000000L)
                        .description("Hình thức thuê theo năm phù hợp cho những khách hàng có nhu cầu đậu xe dài hạn, ví dụ như các doanh nghiệp, các khu chung cư hoặc các khu vực có nhu cầu đậu xe ổn định trong suốt cả năm.")
                        .build();
                rentalFormRepository.save(rentalForm);
            }
            if(parkingZoneRepository.findByName(ZoneEnum.A.name()).isEmpty()){
                ParkingZone parkingZone = ParkingZone.builder()
                        .name(ZoneEnum.A.name()).build();
                parkingZoneRepository.save(parkingZone);
            }
            if(parkingZoneRepository.findByName(ZoneEnum.B.name()).isEmpty()){
                ParkingZone parkingZone = ParkingZone.builder()
                        .name(ZoneEnum.B.name()).build();
                parkingZoneRepository.save(parkingZone);
            }
            if(parkingZoneRepository.findByName(ZoneEnum.C.name()).isEmpty()){
                ParkingZone parkingZone = ParkingZone.builder()
                        .name(ZoneEnum.C.name()).build();
                parkingZoneRepository.save(parkingZone);
            }

        };
    }
}
