// 기본 마커 표시

// var map = new naver.maps.Map('map', {
//     center: new naver.maps.LatLng(37.5112, 127.0981), // 잠실 롯데월드를 중심으로 하는 지도
//     zoom: 15
// });

// var marker = new naver.maps.Marker({
//     position: new naver.maps.LatLng(37.5112, 127.0981),
//     map: map
// });

// -------------------------------------------------

// 다중 마커 표시

$(function () {

    initMap();

});

function initMap() {

    var areaArr = new Array();  // 지역을 담는 배열 ( 지역명/위도경도 )
    areaArr.push(
        /*이름*/			/*위도*/					/*경도*/
        { location: '농담곰', lat: '37.5112', lng: '127.0981' },  // 호스트 좌표
        { location: '북극곰', lat: '37.64355498448049', lng: '127.02785652511915' },  // 참가자1 좌표
        { location: '콜라곰', lat: '37.64355498448049', lng: '127.02785652511915' },  // 참가자2 좌표
        { location: '망그곰', lat: '37.64355498448049', lng: '127.02785652511915' },  // 참가자3 좌표
    );

    let markers = new Array(); // 마커 정보를 담는 배열
    let infoWindows = new Array(); // 정보창을 담는 배열

    var map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(37.5112, 127.0981), //지도 시작 지점 = 호스트 좌표
        zoom: 12
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

    var middlemarker = new naver.maps.Marker({
        map: map,
        position: new naver.maps.LatLng(middlelat, middlelng), // 사용자의 위도 경도 넣기
        title: '중간지점',
        icon: {
            url: 'maker.png',
            size: new naver.maps.Size(50, 52),
            origin: new naver.maps.Point(0, 0),
            anchor: new naver.maps.Point(25, 26)
        }
    });


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