
$(function () {

    initMap();

});



function initMap() {
    // 호스트 좌표 변수
    let main_lat, main_lng;
        main_lat = 37.64359950713993;
        main_lng = 127.02755816582702;
    const name = "꾸브라꼬";

        var areaArr = new Array();  // 지역을 담는 배열 ( 지역명/위도경도 )
        areaArr.push(
            /*이름*/			/*위도*/					/*경도*/
            { location: name, lat: main_lat, lng: main_lng },  // 호스트 좌표
        );


    let markers = new Array(); // 마커 정보를 담는 배열
    let infoWindows = new Array(); // 정보창을 담는 배열


	// v3 버전 지도 생성
	var map = new naver.maps.Map('map_v3', {
		center : new naver.maps.LatLng(37.64359950713993, 127.02755816582702),
		zoom : 15,
		mapTypeControl : true // 일반, 위성 버튼 보이기 (v3 에서 바뀐 방식)
	});


    let Slat = 0, Slng = 0;

    for (var i = 0; i < areaArr.length; i++) {
        Slat += Number(areaArr[i].lat);
        Slng += Number(areaArr[i].lng);
    }

    // 중간지점 마커
    var middlemarker_option = {
        map: map,
        position: new naver.maps.LatLng(main_lat, main_lng), // 사용자의 위도 경도 넣기
        title: '중간지점',
        icon: {
            url: 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png',
            size: new naver.maps.Size(60, 62),
            origin: new naver.maps.Point(0, 0),
            anchor: new naver.maps.Point(25, 26)
        }
    };

    var infoWindow = new naver.maps.InfoWindow({
        content: '<div style="width:10px;text-align:center;padding:10px;"><b>' + areaArr[0].location
    }); // 클릭했을 때 띄워줄 정보 HTML 작성

    markers.push(middlemarker_option); // 생성한 마커를 배열에 담는다.
    infoWindows.push(infoWindow); // 생성한 정보창을 배열에 담는다.



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


        console.log(markers[0], getClickHandler(0));
        naver.maps.Event.addListener(markers[0], 'click', getClickHandler(0)); // 클릭한 마커 핸들러

}