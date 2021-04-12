# GPS_test
 for toy project #1 group navigation

 GPS service test

## 테스트 완료

>2021.03.25
>
### 1. GPS 권한 설정
 > 1-0. GPS 권한 확인 메소드

    // 0. Check GPS Permission
    private boolean checkGPSPermission() {...}

 > 1) GPS 권한 확인    

    // 1. GPS 권한 확인
    private void getGPSPermission() {...}
    
 > 2) 권한 요청 다이얼로그

    // 2. 권한 요청 다이얼로그 결과에 대한 액션
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {...}
    
 > 3) GPS 권한 거부

    // 3. GPS 권한 거부 이벤트
    private void alertPermissionGPS(boolean repeated, String msg) {...}

 > 4) 앱 종료 다이얼로그

    // #. 앱 종료 다이얼로그
    private void alertFinish() {...}
    
 > 5) GPS 권한 설정으로 이동

    // 4. GPS 권한 설정 화면
    private void setGPSPermission() {...}
    
 > 6) GPS 설정 종료 이벤트

    // 5. GPS 권한 설정 종료 이벤트
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {...}

### 2. GPS 활성화
 > 0) GPS 활성 확인 메소드

    // 0. GPS 활성 상태 확인
    private boolean isGPSEnabled() {...}

 > 1) GPS 활성화

    // 1. GPS 활성화
    private void setGPSEnabled() {...}
    
 > 2) GPS 활성화 다이얼로그

    // 2. GPS 사용 설정 다이얼로그
    private void alertCheckGPS() {...}
    
 > 3) GPS 활성화 설정

    // 3. GPS 설정
    private void setGPSConfig() {...}

### 3. GPS 신호 수신
 > 1) 최적 제공자 설정

    // Get Best Provider
    private String getBestProvider() {...}
    
 > 2) Location Listner 설정

    // Location Listener
    public class MLocationListner implements LocationListener {...}
    
 > 3) GPS 신호 수신

    private void getLocation() {...}
    
## 추후 진행 사항

>1. GPS 수신 간격 설정
>2. 서비스로 구현
>3. 로그인 시 권한 및 활성화 설정으로 변경
>4. 로그인 권한 및 활성화 끝나면 GPS 수신 시작하여 초기 위치 
