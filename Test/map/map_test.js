// 다중 마커 표시

$(function () {

    initMap();

});

function initMap() {
    // 호스트 좌표 변수
    let main_lat, main_lng;
    main_lat = 37.64359950713993;
    main_lng = 127.02755816582702;

    var areaArr = new Array();  // 지역을 담는 배열 ( 지역명/위도경도 )
    areaArr.push(
        /*이름*/			/*위도*/					/*경도*/
        { location: '농담곰', lat: main_lat, lng: main_lng },  // 호스트 좌표
        { location: '북극곰', lat: '37.643554984480', lng: '127.027856525115' },  // 참가자1 좌표
        { location: '콜라곰', lat: '37.640668966010516', lng: '127.02720710334465' },  // 참가자2 좌표
        { location: '망그곰', lat: ' 37.64302726452654', lng: '127.02830864442011' },  // 참가자3 좌표
    );

    let markers = new Array(); // 마커 정보를 담는 배열
    let infoWindows = new Array(); // 정보창을 담는 배열

    var map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(main_lat, main_lng), //지도 시작 지점 = 호스트 좌표
        zoom: 18
    });

    let Slat = 0, Slng = 0;

    for (var i = 0; i < areaArr.length; i++) {
        Slat += Number(areaArr[i].lat);
        Slng += Number(areaArr[i].lng);
    }

    let middlelat, middlelng;

    middlelat = Slat / areaArr.length;
    middlelng = Slng / areaArr.length;


    for (var i = 0; i < areaArr.length; i++) {
        // 좌표 배열의 길이만큼 for문으로 마커와 정보창 매칭

        var marker = new naver.maps.Marker({
            map: map,
            title: areaArr[i].location, // 사용자 이름 
            position: new naver.maps.LatLng(areaArr[i].lat, areaArr[i].lng) // 사용자의 위도 경도 넣기 
        });

        /* 정보창 */
        var infoWindow = new naver.maps.InfoWindow({
            content: '<div style="width:150px;text-align:center;padding:10px;"><b>' + areaArr[i].location
        }); // 클릭했을 때 띄워줄 정보 HTML 작성

        markers.push(marker); // 생성한 마커를 배열에 담는다.
        infoWindows.push(infoWindow); // 생성한 정보창을 배열에 담는다.
    }

    // 중간지점 마커
    var middlemarker_option = {
        map: map,
        position: new naver.maps.LatLng(middlelat, middlelng), // 사용자의 위도 경도 넣기
        title: '중간지점',
        icon: {
            url: 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png',
            size: new naver.maps.Size(60, 62),
            origin: new naver.maps.Point(0, 0),
            anchor: new naver.maps.Point(25, 26)
        }
    };

    var middlePoint = new naver.maps.Marker(middlemarker_option);


    function getClickHandler(seq) {

        return function (e) {  // 마커를 클릭하는 부분
            var marker = markers[seq], // 클릭한 마커의 시퀀스로 찾는다.
                infoWindow = infoWindows[seq]; // 클릭한 마커의 시퀀스로 찾는다

            if (infoWindow.getMap()) {
                infoWindow.close();
            } else {
                infoWindow.open(map, marker); // 표출
            }
            map.panTo(e.coord); // 마커 클릭시 부드럽게 이동
        }
    }

    for (var i = 0, ii = markers.length; i < ii; i++) {
        console.log(markers[i], getClickHandler(i));
        naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i)); // 클릭한 마커 핸들러
    }

}