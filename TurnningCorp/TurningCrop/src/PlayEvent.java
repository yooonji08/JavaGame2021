import java.util.Scanner;

/*
 * object: 방 내부에 있는 대표적 사물들 (부장 책상, 정돈된 책상 ,,,)
 * object 안의 item: 실제 아이템... (책상 위의 메모지, 일기장, 사탕 껍데기 ,,,)
 */

/*
 * 오기입 관련해서는 익셉션 처리로 넘길 수도 있음...아직 고민중
 * 또한 일정 부분 메소드로 따로 만들어 처리할 수도 있음(반복되는 부분)
 */

public class PlayEvent {
	// 스캐너 객체, 선택지/오브젝트 임시 저장 변수 생성
	private Scanner scan = new Scanner(System.in);
	private int num;
	private MapObject mapObject;
	
	// 타이틀 이동 판별
	private boolean goTitle;
	
	// 엔딩 판별할 때 사용하는 포인트 값
//	private int endingPoint = 0;
	
	// Player, Map 객체
	// -> GameManager 클래스에서 PlayEvent 객체 생성할 때 집어넣기
	private Map[][] map;
	private Player player;
	
	
	// 생성자
	PlayEvent()
	{
		// 임시,,
	}
	PlayEvent(Map[][] map, Player player)
	{
		this.map = map;
		this.player = player;
	}
	
	// getter, setter 메소드
	public boolean getGoTitle()
	{
		return goTitle;
	}
	public void setGoTitle(boolean goTitle)
	{
		this.goTitle = goTitle;
	}
	
	
	// 층간 이동 보조 (엘리베이터)
	private void moveFloor()
	{
		while(true)
		{
			// 층 목록 보여주기
			// ex) 1층(활성화) \n ...
			for (int i = 0; i < 5; i++)
			{
				System.out.println(map[i][0].printActive());
			}
			
			System.out.println("이동할 층의 버튼을 누르자. (숫자 입력)");
			
			num = scan.nextInt();
			
			// 만일 누른 층이 비활성화 상태라면
			if (!map[num - 1][0].getActive())
			{	
				// 이동 실패, 다시 층 목록 출력으로 돌아감
				System.out.println("버튼이 눌리지 않는다.");
				continue;
			}
			
			if (num == 1)
			{
				player.setPosID(10);	// 모두 로비로 이동하는 것이기 때문에 모든 ID의 뒷 번호는 0이다.
				break;
			}
			else if (num == 2)
			{
				player.setPosID(20);
				break;
			}
			else if (num == 3)
			{
				player.setPosID(30);
				break;
			}
			else if (num == 4)
			{
				player.setPosID(40);
				break;
			}
			else if (num == 5)
			{
				player.setPosID(50);
				break;
			}
			else if (num == 666)
			{
				System.out.println("불가능하다.");
				continue;
			}
			else
			{
				System.out.println("그런 버튼은 없다.");
				continue;
			}
		}
	}
	
	
	// 사물(object 출력 템플릿)
	// 해당 오브젝트를 받아와 출력함
	public void objectPrint(MapObject mapObject)
	{
		// ex) 부장님의 책상이다.
		System.out.println(mapObject.getObjectName() + "이다.");
		// 해당 오브젝트의 설명 출력
		System.out.println(mapObject.getDescription());
	}
	
	
	// 일반 방에 진입했을 때 출력/이벤트 진행(반복 출력) - 로비 제외
	// true 반환: 해당 방 이벤트 메소드에서 그대로 사물 조사 진행
	// false 반환: 해당 방 이벤트 메소드의 반복문을 탈출, 로비로 나가게 함
	public boolean enterRoom(int floor, int roomID)
	{
		// 배열의 인덱스로 활용하기 위해 1 감소
		floor--;
		
		while(true)
		{
			System.out.println("어디를 살펴볼까?");
			
			// for문으로 해당 방의 조사 사물 배열 목록 (숫자와 함께) 출력하기..
			for (int i = 0; i < map[floor][roomID].getAllObject().length; i++)
			{	// ex) 1. 내 책상
				System.out.println((i + 1) + ". " + map[floor][roomID].getObject(i).getObjectName());
			}
			// [마지막 번호]. 로비로 나간다.
			System.out.println((map[floor][roomID].getAllObject().length + 1) + ". 로비로 나간다.");
			System.out.print("* 원하는 선택지의 숫자 입력: ");
			
			num = scan.nextInt();
			
			// 선택지 안의 번호를 제대로 입력하지 않았을 경우(오기입)
			if (!(num >= 1 && num <= map[floor][roomID].getAllObject().length + 1))
			{
				System.out.println("그쪽은 조사가 불가능하다.");
				continue;
			}
			
			// 선택지를 제대로 입력했을 경우, 위에서 continue가 걸리지 않으므로 break 가능
			// 현재 while문 탈출하여 정상 진행하도록 함
			break;
		}
		
		// 로비로 나가기를 선택했을 경우
		if (num == map[floor][roomID].getAllObject().length + 1)
		{
			// floor가 인덱스로 활용되기 위해 1 감소했었으므로
			// 다시 1 증가 시킨 후, 수식 계산을 통해 10이나 20 등의 posID 형태로 변환함
			int id = (floor + 1) * 10 + roomID;
			
			// 로비로 위치 재조정
			player.setPosID(id);
			// 해당 방 메소드(enterRoom()이 호출되는 곳)의 반복문을 탈출할 수 있도록 함
			return false;
		}
		
		// 선택지를 제대로 입력했을 경우 (기본 설정)
		// 1. 현재 오브젝트를 선택한 오브젝트로 설정
		mapObject = map[floor][roomID].getObject(num - 1);
		
		// 2. 해당 오브젝트의 이름 & 설명 출력
		// ex) 부장님의 책상과 그 책상에 대한 설명 출력..
		objectPrint(mapObject);
		
		return true;
	}
	
	
	// ----------------------------------------------------------------- \\
	
	
	// 1층 로비 이벤트 함수
	public void playFloor1_0()
	{
		while(true)
		{
			// 1. 로비 스크립트 출력
			// 로비다! (스크립트 수정 필요)
			
			// 2. 선택지 출력
			System.out.println("1. 출입기에 사원증을 찍자.");	// 저장
			System.out.println("2. 잠깐 밖에서 쉬고 오자.");	// 타이틀로 나가기 (게임 메뉴)
			System.out.println("3. 다른 곳을 둘러보자.");		// 다른 방 이동
			System.out.println("4. 엘리베이터를 타자.");		// 층 이동
			
			num = scan.nextInt();
			
			if (num == 1)			// 저장
			{
				// 현재 게임 데이터 로컬에 저장
				
				System.out.println("사원증을 성공적으로 찍었다!\n--저장되었습니다.--");
			}
			else if (num == 2)		// 저장 후 타이틀로 나가기
			{
				// 1. 현재 데이터 로컬에 저장
				
				// 2. 타이틀로 나가기
				goTitle = true;
				break;
			}
			else if (num == 3)		// 다른 방으로 이동
			{
				// 영업부, 카페, 경비실이 보인다. 어디로 들어갈까?
				System.out.println("1. 영업부로 들어가자.");
				System.out.println("2. 카페로 들어가자.");
				System.out.println("3. 경비실로 들어가자.");
				System.out.println("4. 생각이 달라졌다.");		// 다시 로비 선택지 출력
				
				num = scan.nextInt();
				
				if (num == 1)	// 영업부 들어가기
				{
					player.setPosID(11);
					break;
				}
				else if (num == 2)	// 카페 들어가기
				{
					player.setPosID(12);
					break;
				}
				else if (num == 3)	// 경비실 들어가기
				{
					player.setPosID(13);
					break;
				}
				else if (num == 4)	// 다른 방으로 가지 않기(로비 스크립트/선택지 다시 출력)
				{
					continue;
				}
				else
				{
					// 그 외 오기입 처리
				}
			}
			else if (num == 4)		// 다른 층으로 이동(엘리베이터 탑승)
			{
				// 1. 보스몬스터(케로베로스) 처치 판별
				// (처치하지 않았다면) 플레이어의 평판도 검사 -> 전투/그냥 물러남
				
				// 2. (처치했다면) 층 이동 메소드 수행
				moveFloor();
				break;
			}
			else
			{
				// 그 외 오기입 처리
			}
		}
	}
	
	// 1층 영업부(방1) 이벤트 함수
	public void playFloor1_1()
	{
		// 1. 배틀....확률 돌리기
		
		// 2. 영업부 스크립트 출력...
		System.out.println("영업부다.");
		System.out.println("내가 일하는 부서라 지긋하리만치 익숙할 만한데, 묘하게 서늘한 기운이 목을 감싼다. 밤이라 그런가.");
		System.out.println("앞에는 익숙한 책상 배열들이 보인다. 개중에는 눈에 띄는 책상도 심심찮게 있다.");
		
		while(true)
		{
			// 1층의 첫 번째(1) 방이므로 인수에 1, 1을 집어넣음
			// 방 진입 이벤트 (사물(오브젝트) 출력 및 선택 진행)
			// enterRoom의 반환값이 false면 현재 방의 이벤트를 종료하도록 함(이동)
			if (!enterRoom(1, 1))
			{
				break;
			}
			
			
			// enterRoom의 반환값이 true일 경우
			// -> 정상 진행
			if (num == 1)		// 부장 책상
			{
				// 책상 위에는 메모지가 있다.
				System.out.println("\n책상 위에는 " + mapObject.getItem(0).getName() + "가 있다.");
				
				System.out.println("자세히 볼까?");
				System.out.println("1. 본다.");
				System.out.println("2. 안 본다.");
				
				num = scan.nextInt();
				
				if (num == 1)		// 본다
				{	// 메모지 내용 출력
					// 사장님 결재 서류는 이메일로 보내세요.
					System.out.println(mapObject.getItem(0).getDescription());
				}
				else if (num == 2)	// 안 본다
				{
					System.out.println("안 보기로 했다. 상사의 메모는 훔쳐보면 안 된다.");
					System.out.println("다른 책상을 살펴보자.");
				}
				else				// 오기입
				{
					System.out.println("그런 선택지는 없다.");
				}
			}
			else if (num == 2)	// 수상한 사원의 책상
			{
				while(true)
				{
					System.out.println("책상 위와 책상 아래 서랍 중 어느 곳을 먼저 볼까?");
					System.out.println("1. 책상 위를 보자.");
					System.out.println("2. 책상 아래 서랍을 보자.");
					System.out.println("3. 안 볼 것이다. (책상 목록으로 나가기)\n");
					
					num = scan.nextInt();
					
					if (num == 1)		// 책상 위 조사
					{
						// 이미 아이템을 가져갔는지 검사(일기장)
						if (player.searchItem(mapObject.getItem(0).getName()))
						{
							// 아이템을 이미 가져갔다면 다시 책상 위/책상 아래 선택지로 이동
							System.out.println("책상 위에 더이상 특이한 건 보이지 않는다.");
							continue;
						}
						
						// 아이템을 가져가지 않았다면 기존 진행 그대로..
						
						// 책상 위에는 일기장이 있다.
						System.out.println("책상 위에는 " + mapObject.getItem(0).getName() + "이 있다.");
						System.out.println("가져갈까?");
						System.out.println("1. 가져가자.");
						System.out.println("2. 가져가지 말자.");
						
						num = scan.nextInt();
						
						if (num == 1)
						{
							// 플레이어 인벤토리에 아이템 저장
							player.saveInventory(mapObject.getItem(0));
							
							System.out.println(mapObject.getItem(0).getName() + "을 가져갔다.");
						}
						else if (num == 2)
						{
							System.out.println("가져가지 않기로 했다.");
						}
						else				// 오기입
						{
							System.out.println("그런 선택지는 없다.");
						}
					}
					else if (num == 2)	// 책상 아래 서랍 조사
					{
						// '모르는 열쇠'를 갖고 있는지 검사
						// 갖고 있다면
						if (player.searchItem("모르는 열쇠"))
						{
							System.out.print("책상 서랍 안에는 ");
							
							// 해당 아이템을 갖고 갔는지 검사(쉽배악1)
							if (player.searchItem(mapObject.getItem(1).getName()))
							{
								System.out.println("더이상 아무 것도 없다.");
							}
							else	// 아직 가져가지 않았다면
							{	
								System.out.println(mapObject.getItem(1).getName() + "이 있다.");
								System.out.println("가져갈까?");
								System.out.println("1. 가져가자.");
								System.out.println("2. 가져가지 말자.");
								
								num = scan.nextInt();
								
								if (num == 1)		// 가져가자
								{
									// 플레이어 인벤토리에 아이템 저장
									player.saveInventory(mapObject.getItem(1));
									
									System.out.println(mapObject.getItem(1).getName() + "을 가져갔다.");
								}
								else if (num == 2)	// 가져가지 말자
								{
									System.out.println("가져가지 않기로 했다.");
								}
								else				// 오기입
								{
									System.out.println("그런 선택지는 없다.");
								}
							}
						}
						else	// 열쇠가 없다면
						{
							System.out.println("서랍이 열리지 않는다.");
							System.out.println("자물쇠로 단단히 잠겨있다. 열쇠가 필요할 것 같다.");
						}
					}
					else if (num == 3)	// 안 보기
					{
						System.out.println("아무리 수상하더라도 남의 책상을 보면 안 된다.");
						System.out.println("수상한 사원의 책상에서 나왔다.");
						break;		// 반복문 탈출, 책상 목록들 선택지로 이동함
					}
					else				// 오기입
					{
						System.out.println("그런 선택지는 없다.");
					}
				}
			}
			else if (num == 3)	// 어지러운 책상
			{
				// 서류더미 이름, 설명 출력
				System.out.println("책상 위에는 " + mapObject.getItem(0).getName() + "가 있다.");
				System.out.println(mapObject.getItem(0).getDescription());
				
				System.out.println("그 외에 별다른 것은 없어보인다.");
				System.out.println("사실상 책상 위가 어지러워 뭐가 뭔지 알아보기가 힘들다. 나가자.");
			}
			else if (num == 4)	// 정돈된 책상
			{
				// 다이어리가 있다!
				System.out.println("책상 위에는 " + mapObject.getItem(0).getName() + "가 있다.");
				System.out.println("자세히 볼까?");
				System.out.println("1. 본다.");
				System.out.println("2. 안 본다.");
				
				num = scan.nextInt();
				
				if (num == 1)		// 본다
				{	
					// 다이어리의 설명 출력
					System.out.println(mapObject.getItem(0).getDescription());
				}
				else if (num == 2)	// 안 본다
				{
					System.out.println("안 보기로 했다.");
					System.out.println("다른 책상들을 보러가자.");
				}
				else				// 오기입
				{
					System.out.println("그런 선택지는 없다.");
				}
			}
			else if (num == 5)	// 배부른 책상.....?
			{	
				// '사탕 껍데기' 출력
				System.out.print(mapObject.getItem(0).getName());
				
				// 안 뜯은 과자봉지를 가져갔는지 검사
				if (player.searchItem(mapObject.getItem(1).getName()))
				{
					// 가져갔다면 사탕 껍데기에 대한 이름/설명만 출력
					System.out.println("가 있다.");
					System.out.println(mapObject.getItem(0).getDescription());
				}
				else	// 안 가져갔다면
				{
					// 사탕 껍데기와 안 뜯은 과자봉지가 있다.
					System.out.println("와 " + mapObject.getItem(1).getName() + "가 있다.");
					
					System.out.println("어느 것을 먼저 볼까?");
					// 선택지 목록 출력(사탕 껍데기, 과자봉지,,,)
					for (int i = 0; i < 2; i++)
						System.out.println((i + 1) + ". " + mapObject.getItem(i).getName());
//					System.out.println("3. 안 보기");	--> 선택지 번호..유동성있게 수정 필요
					
					num = scan.nextInt();
					
					if (num == 1)		// 1. 사탕 껍데기 선택
					{
						// 사탕 껍데기 설명 출력
						System.out.println(mapObject.getItem(0).getDescription());
					}
					else if (num == 2)	// 2. 안 뜯은 과자봉지 선택
					{
						// 안 뜯은 과자봉지 설명 출력
						System.out.println(mapObject.getItem(1).getDescription());
						System.out.println("가져갈까?");
						System.out.println("1. 가져가자.");
						System.out.println("2. 냅두자.");
						
						num = scan.nextInt();
						
						if (num == 1)		// 가져가자
						{
							// 플레이어 인벤토리에 아이템 저장
							player.saveInventory(mapObject.getItem(1));
							
							System.out.println(mapObject.getItem(1).getName() + "를 챙겼다.");
						}
						else if (num == 2)	// 냅두자
						{
							System.out.println("냅두기로 했다.");
							System.out.println("다른 책상이나 둘러보자.");
						}
						else				// 오기입
						{
							System.out.println("그런 선택지는 없다.");
						}
					}
					else	// 그 외 오기입
					{
						System.out.println("선택지를 잘못 입력한 듯 하다.");
					}
				}
			}
			else if (num == 6)	// 플레이어의 책상
			{
				System.out.println("책상 서랍을 열어볼까?");
				System.out.println("1. 열어보자.");
				System.out.println("2. 열지말자.");
				
				num = scan.nextInt();
				
				if (num == 1)		// 서랍 열기
				{
					// 모르는 열쇠를 가져갔다면
					if (player.searchItem(mapObject.getItem(0).getName()))
					{
						System.out.println("아무 것도 들어있지 않다. 평소에 무언가를 좀 넣어둘 걸 그랬나보다.");
					}
					// 아직 가져가지 않았다면
					else
					{	
						// 모르는 열쇠가 들어있다.
						System.out.println(mapObject.getItem(0).getName() + "가 들어있다. 이게 뭐지?");
						System.out.println("가져가볼까?");
						System.out.println("1. 가져가자.");
						System.out.println("2. 냅두자.");
						
						num = scan.nextInt();
						
						if (num == 1)		// 가져가자
						{
							// 플레이어 인벤토리에 아이템 저장
							player.saveInventory(mapObject.getItem(0));
							
							System.out.println(mapObject.getItem(0).getName() + "를 챙겼다.");
						}
						else if (num == 2)	// 냅두자
						{
							System.out.println("냅두기로 했다.");
							System.out.println("다른 책상이나 둘러보자.");
						}
						else				// 오기입
						{
							System.out.println("그런 선택지는 없다.");
						}
					}
				}
				else if (num == 2)		// 서랍 열지 않기
				{
					System.out.println("열지 않기로 했다.");
					System.out.println("다른 책상이나 좀 더 둘러보자.");
				}
				else				// 오기입
				{
					System.out.println("그런 선택지는 없다.");
				}
			}
		}
	}
	
	// 1층 카페(방2) 이벤트 함수
	public void playFloor1_2()
	{
		// 1. 배틀....확률 돌리기
		
		// 2. 카페 스크립트 출력...
		System.out.println("카페다.");
		System.out.println("낮에는 붐비지는 않더라도 항상 사람 두어명씩은 꼭 있던 곳이었는데");
		System.out.println("이렇게 한산한 모습을 보니 기분이 묘하다.");
		System.out.println("잔잔한 커피향만이 코 끝에 맴돈다.");
		
		while(true)
		{
			// 1층의 두 번째(2) 방이므로 인수에 1, 2를 집어넣음
			// 방 진입 이벤트 (사물(오브젝트) 출력 및 선택 진행)
			// enterRoom의 반환값이 false면 현재 방의 이벤트를 종료하도록 함(이동)
			if (!enterRoom(1, 2))
			{
				break;
			}
			
			// enterRoom의 반환값이 true일 경우
			// -> 정상 진행
			if (num == 1 || num == 2)			// TV 1, 2 조사
			{
				System.out.println("(지직..)");
				System.out.println("밤에 봐서 그런가..? TV가 묘하게 이상하다.");
			}
			else if (num >= 3 && num <= 5)		// 책상 1, 2, 3 조사
			{
				System.out.println("아무 것도 없다.");
			}
			else if (num == 6)					// 카운터 조사
			{
				// 쉽배악2 흭득 가능 장소
				System.out.println("포스기다.");
				
				System.out.println("옆에는 포스트잇이 붙어있다.");
				System.out.println("무언가 크게 휘갈겨 써 있다.");
				System.out.println("'비밀번호는 이 방 $&@# 개수다.'");
				System.out.println("..심한 악필이라 중간 부분은 알아볼 수가 없었다.");
				
				System.out.println("아래 쪽에는 비밀번호를 입력하는 곳인 것 같다.");
				System.out.println("뭐라고 입력하면 될까?");
				System.out.print("* 한 자리 숫자를 입력해보자: ");
				
				num = scan.nextInt();
				
				// 현재 방의 책상 개수인 3 입력(정답)
				if (num == 3)
				{
					System.out.println("(철컥)");
					System.out.println("열렸다! 비밀번호는 책상 개수였다!");
					
					// 이미 아이템을 들고 갔다면(갖고 있다면)
					if (player.searchItem(mapObject.getItem(0).getName()))
					{
						System.out.println("그렇지만 더이상 아무 것도 들어있지 않다...");
						System.out.println("누가 보면 오해할 수 있으니 다시 닫아 놓자.");
						System.out.println("(다시 잠기는 소리)");
						
						// 다시 사물 선택지 출력(처음)으로 되돌아감
						continue;
					}
					
					while(true)
					{
						// 아이템을 아직 습득하지 않았다면
						System.out.println(mapObject.getItem(0).getName() + "다.");
						System.out.println("가져갈까?");
						System.out.println("1. 가져가자.");
						System.out.println("2. 가져가지 말자.");
						
						num = scan.nextInt();
						
						if (num == 1)		// 가져가기
						{
							// 쉽게 배우는 악마어2 습득
							player.saveInventory(mapObject.getItem(0));
							
							System.out.println(mapObject.getItem(0).getName() + "을 챙겼다.");
						}
						else if (num == 2)	// 가져가지 않기
						{
							
						}
						else				// 오기입
						{
							System.out.println("애매한 선택은 하지 말자.");
							
							continue;
						}
						
						// 오기입 외의 정상 진행일 경우
						System.out.println("혹시 모르니 다시 포스기를 닫았다.");
						System.out.println("(다시 잠기는 소리)");
						
						break;
					}
				}
				else if (num >= 10)		// 한 자리수가 아닌 그 이상을 입력했을 경우
				{
					System.out.println("..한 자리 숫자라고 하지 않았나?");
				}
				else if (num < 0)		// 음수를 입력했을 경우
				{
					System.out.println("비밀번호가..마이너스일 리는 없을 것 같다.");
				}
				else					// 그 외 오답
				{
					System.out.println("아무 일도 일어나지 않았다.");
				}
			}
		}
	}
	
	// 1층 경비실(방3) 이벤트 함수
	public void playFloor1_3(SkillNPC npc)
	{
		// 1. 배틀....확률 돌리기
		
		// 2. 경비실 스크립트 출력
		System.out.println("경비실이다.");
		System.out.println("잔잔한 어둠 사이로 희미한 모니터 불빛이 새어나오고 있다.");
		System.out.println("그리고 그 불빛 옆에는.. 한 사람이 앉아있다.");
		
		while(true)
		{
			// 이 방은 스킬 NPC만 존재하는 방이므로 오브젝트(사물)가 따로 존재하지 않음
			// 따라서 enterRoom() 메소드를 호출하지 않음
			
			// ex) "어서오세요, 평범한 사원 @@님."
			System.out.println("\"어서오세요, " + player.getTitleArray(player.getCurrentIndex()) + "@@님.\"");
			System.out.println("\"무엇을 하시겠습니까?\"");
			System.out.println("\"현재 @@님의 진급 여부를 확인할 수 있습니다.\"");
			
			System.out.println("1. 진급을 확인하자.");		// 플레이어의 등급 & 스킬 업데이트
			System.out.println("2. 진급이 뭔데?");			// 진급 설명 듣기
			System.out.println("3. 나갈래.");				// 로비로 나가기
			
			num = scan.nextInt();
			
			if (num == 1)			// 진급 여부 확인 & 등급/스킬 업데이트
			{
				// 퀘스트(등급) 단계에 따른 NPC의 스크립트 출력
				System.out.println(npc.playQuestScript(npc.playQuest(player)));
				
				// 만약 진급에 성공했다면 (현재 퀘스트를 완료했다면)
				if (npc.getQuest(npc.getQuestCount()).getCompletion())
				{
					// 현재 스킬을 해당 스킬로 교체(player의 메소드 활용)
					
					// 플레이어가 확인할 수 있는 문구...(스킬이 교체됨을 알림)
					// "이제 ooo, ㅁㅁㅁ라고 말씀하시면 됩니다."
				}
			}
			else if (num == 2)		// 진급/스킬에 관한 설명 듣기
			{
				// 아직 어떻게 설명을 넣어야 깔끔할지 모르겠음,,고민중
				// 대략 진급을 하면 더 높은 등급의 말하기 스킬을 쓸 수 있다는 의미,,,
				System.out.println("\" ~~~ \"");
			}
			else if (num == 3)		// 로비로 나가기
			{
				player.setPosID(10);
				break;
			}
			else					// 오기입
			{
				System.out.println("\"그런 건 하실 수 없습니다.\"");
			}
		}
	}
	
	// 1층 화장실(posID: 14) 이벤트 함수
	public void playFloor1_4()
	{
		// 1. 배틀....확률 돌리기
		
		// 2. 영업부 스크립트 출력...
		System.out.println("영업부다.");
		System.out.println("내가 일하는 부서라 지긋하리만치 익숙할 만한데, 묘하게 서늘한 기운이 목을 감싼다. 밤이라 그런가.");
		System.out.println("앞에는 익숙한 책상 배열들이 보인다. 개중에는 눈에 띄는 책상도 심심찮게 있다.");
		
		while(true)
		{
			// 1층의 네 번째(4) 방(화장실)이므로 인수에 1, 4를 집어넣음
			// 방 진입 이벤트 (사물(오브젝트) 출력 및 선택 진행)
			// enterRoom의 반환값이 false면 현재 방의 이벤트를 종료하도록 함(이동)
			if (!enterRoom(1, 4))
			{
				break;
			}
			
			
			// enterRoom의 반환값이 true일 경우
			// -> 정상 진행
			if (num == 1)		// 거울 보기
			{
				System.out.println("내 모습이다.");
				System.out.println("* hp: " + player.getHP());
				System.out.println("* 공격력: " + player.getAttackPower());
				System.out.println("* 방어력: " + player.getDefensivePower());
				// 공격력과 방어력의 이름을 좀 더 뭔가... 자연스럽게 바꾸고 싶은데 생각이 안 남(고민중)
				System.out.println("* 평판도: " + player.getReputation());
			}
			else if (num == 2)	// 바닥 보기
			{
				// 만약 플레이어가 이미 볼펜을 주웠다면
				if (player.searchItem(mapObject.getItem(0).getName()))
				{
					System.out.println("언제나 그랬듯이 깨끗한 바닥이다.");
					
					// 사물(오브젝트) 선택지로 다시 돌아감
					continue;
				}
				
				// 볼펜을 아직 안 주웠다면
				System.out.println("바닥에 볼펜 한 개가 떨어져 있다.");
				System.out.println("정말... 사무적으로 생겼다.");
				
				System.out.println("가져갈까?");
				System.out.println("1. 가져가자");
				System.out.println("2. 냅두자.");
				
				num = scan.nextInt();
				
				if (num == 1)		// 가져가기
				{
					// 볼펜을 플레이어의 인벤토리에 저장
					player.saveInventory(mapObject.getItem(0));
					
					System.out.println("볼펜을 챙겼다.");
					System.out.println("볼펜을 주운 손이 살짝 축축한 것 같기도 하다..");
				}
				else if (num == 2)	// 냅두기
				{
					System.out.println("완전..사무적으로 생긴 볼펜을 바닥에 내버려두었다.");
					System.out.println("혼자 떨어져 있는 모습이 꽤 처량해 보이기도 하지만 어쩔 수 없다.");
				}
				else				// 오기입
				{
					System.out.println("선택은 확실히 해야한다.");
				}
			}
			else if (num == 3)	// 내 가방 보기(인벤토리, 현재 진행중인 퀘스트 확인)
			{
				System.out.println("어떤 걸 볼까?");
				System.out.println("1. 물건 위주");		// 인벤토리 확인
				System.out.println("2. 할 일 목록");	// 퀘스트 목록 확인
				
				num = scan.nextInt();
				
				if (num == 1)			// 인벤토리 확인
				{
					player.showInventory();
				}
				else if (num == 2)		// 퀘스트 확인
				{
					// 현재 플레이어가 진행 중인 퀘스트 목록 출력
					player.printQuestList();
				}
				else					// 오기입
				{
					System.out.println("그건... 잘못된 선택이다.");
				}
			}
		}
	}
	
	
	// * * * * * * * * * * * * * \\
	
	
	// 2층 로비 이벤트 함수
	public void playFloor2_0()
	{

		while(true)
		{
			// 1. 로비 스크립트 출력
			// 식당인 2층의 로비다.
			
			// 2. 선택지 출력
			System.out.println("1. 출입기에 사원증을 찍자.");	// 저장
			System.out.println("2. 잠깐 밖에서 쉬고 오자.");	// 타이틀로 나가기 (게임 메뉴)
			System.out.println("3. 다른 곳을 둘러보자.");		// 다른 방 이동
			System.out.println("4. 엘리베이터를 타자.");		// 층 이동
			
			num = scan.nextInt();
			
			if (num == 1)			// 저장
			{
				// 현재 게임 데이터 로컬에 저장
				
				System.out.println("사원증을 성공적으로 찍었다!\n--저장되었습니다.--");
			}
			else if (num == 2)		// 저장 후 타이틀로 나가기
			{
				// 1. 현재 데이터 로컬에 저장
				
				// 2. 타이틀로 나가기
				goTitle = true;
				break;
			}
			else if (num == 3)		// 다른 방으로 이동
			{
				// 취식실, 영양사 사무실이 보인다. 어디로 들어갈까?
				System.out.println("1. 취식실로 들어가자.");
				System.out.println("2. 영양사 사무실로 들어가자.");
				System.out.println("3. 화장실에 가자.");
				System.out.println("3. 생각이 달라졌다.");		// 다시 로비 선택지 출력
				
				num = scan.nextInt();
				
				if (num == 1)		// 취식실 들어가기
				{
					player.setPosID(21);
					break;
				}
				else if (num == 2)	// 영양사 사무실 들어가기
				{
					player.setPosID(22);
					break;
				}
				else if (num == 2)	// 화장실 가기
				{
					player.setPosID(24);
					break;
				}
				else if (num == 3)	// 다른 방으로 가지 않기(로비 스크립트/선택지 다시 출력)
				{
					continue;
				}
				else
				{
					// 그 외 오기입 처리
				}
			}
			else if (num == 4)		// 다른 층으로 이동(엘리베이터 탑승)
			{
				moveFloor();
				break;
			}
			else
			{
				// 그 외 오기입 처리
			}
		}
	}
	
	// 2층 취식실(방1) 이벤트 함수
	public void playFloor2_1()
	{
		// 1. 배틀 시스템 확률
		
		// 2. 취식실 스크립트 출력
		// 식당의 취식실이다. 사내 식당은 맛이 없기로 유명해서, 사원 대부분이 점심은 밖에서 해결해 원래 인적이 드문 곳이다. 오늘도 별 다를 건 없어 보인다.
		// 아무도 없는 취식실에는 배식대와 테이블, 식수대가 보인다.
		
		while(true)
		{
			// 2층의 첫 번째(1) 방이므로 인수에 2, 1을 집어넣음
			// 방 진입 이벤트 (사물(오브젝트) 출력 및 선택 진행)
			// enterRoom의 반환값이 false면 현재 방의 이벤트를 종료하도록 함(이동)
			if (!enterRoom(2, 1))
			{
				break;
			}
			
			
			// enterRoom의 반환값이 true일 경우
			// -> 정상 진행
			if (num == 1)			//테이블 조사
			//2층 1번째 방 획득 아이템 : 빵(2번째 obj의 0), 쉽배악(2번째 obj의 1), 물컵(3번쨰 obj의 0)
			{
				while(true)
				{
					System.out.println("취식실 한가운데 커다란 테이블이 있다. 어딜 살펴볼까?");
					System.out.println("1. 테이블 위를 살펴보자.");
					System.out.println("2. 테이블 아래를 살펴보자.");
					System.out.println("3. 테이블 옆 의자들을	 살펴보자.");
					System.out.println("4. 그만 살펴보자.");
					
					num = scan.nextInt();
					
					if (num == 1) 			//테이블 위를 살펴볼 경우
					{
						System.out.println("테이블 위에는 작은 화분들이 간격을 두고 놓여 있다. 조화는 아니고, 살아 있는 식물 같다.");

						//물컵을 얻었는지 확인. 물컵이 있다면~ **NPC 퀘스트
						if (player.searchItem(map[1][1].getObject(2).getItem(1).getName()))
						{
							System.out.println("마침 식수대에서 얻었던 물이 있다. 이걸로 물을 줄까?");
							System.out.println("1. 물을 준다.");					
							System.out.println("2. 물을 주지 않는다.");
							
							num = scan.nextInt();
							
							if (num == 1)
							{
								System.out.println("화분들에 물을 줬다. 식물들이 기뻐하듯 반짝거린다.");
								player.deleteInventory(map[1][1].getObject(2).getItem(1));		//인벤토리에서 아이템(물컵) 삭제
								
								//퀘스트 완료 처리...
							}
							else if (num == 2)
							{
								System.out.println("다른 곳에 쓸 일이 있을지도 모른다. 넘어가자.");
							}
							else
							{
								System.out.println("물을 줄지, 주지 않을 지 확실히 정하자.");	
							}
						}
					}
					else if (num == 2)		//테이블 아래를 살펴볼 경우
					{
						// 이미 아이템(빵)을 가져갔는지 검사
						// 아이템을 이미 가져갔다면 
						if (player.searchItem(mapObject.getItem(0).getName()))
						{
							System.out.println("테이블 아래에 더 이상 특별한 건 보이지 않는다.");
							continue;
						}
						
						// 아이템(빵)을 가져가지 않았다면
						else {
							System.out.println("테이블 아래에 " + mapObject.getItem(0).getName() + " 하나가 있다. 누가 떨어뜨렸나?");
							
							System.out.println("가져갈까?");
							System.out.println("1. 가져가자.");
							System.out.println("2. 가져가지 말자.");
							
							num = scan.nextInt();
							
							if (num == 1)		//1. 가져간다. 를 선택했을 경우
							{
								// 플레이어 인벤토리에 아이템(빵) 저장
								player.saveInventory(mapObject.getItem(0));
								
								System.out.println("혹시 쓸 곳이 있을지도 모른다. 가져가자.\n" + mapObject.getItem(0).getName() + "을 챙겼다.");
							}
							else if (num == 2)	//2. 가져가지 않는다. 를 선택했을 경우
							{
								System.out.println("그냥 쓰레기 같다. 챙기지 말자.");
							}
							else				//선택지 외 번호 입력 시
							{
								System.out.println("어떻게 하자는 거지? 확실히 정하자.");
							}
						}
					}
					else if (num == 3)		//테이블 옆 의자들을 살펴볼 경우
					{
						System.out.println("테이블을 기준으로 의자들이 줄지어 놓여져 있다.\n둥근 의자와 네모난 의자가 섞여 놓여 있다.");
						System.out.println("배치가 신기하네. 벽 옆에 배치도가 붙어있는데, 살펴볼까?");
						System.out.println("1. 살펴본다.");
						System.out.println("2. 살펴보지 않는다.");
						
						num = scan.nextInt();
						
						if (num == 1)		//살펴볼 경우
						{
							System.out.println("--식당 취실식 배치도--");
							//ONTV
							System.out.println("■■■■■ ■■○○■ ■■■■■ ■○○○■");
							System.out.println("■○○○■ ■○■○■ ○○■○○ ○■○■○");
							System.out.println("■■■■■ ■○○■■ ○○■○○ ○○■○○");
						}
						else if (num == 2)	//살펴보지 않을 경우
						{
							System.out.println("중요하지 않아 보인다. 그냥 넘어가자.");
						}
						else				//선택지 외의 번호를 입력할 경우
						{
							System.out.println("볼 건지, 보지 않을 건지 확실히 하자.");
							
						}
					}
				}
			}
			else if (num == 2)		//배식대 조사
			{
				while(true)
				{
					System.out.println("배식대를 살펴봤다. \n깨끗하게 정리된 배식대에는 4개의 배식통이 올려져 있다. 열어볼까?");
					System.out.println("1. 첫 번째 통을 열어보자.");
					System.out.println("2. 두 번째 통을 열어보자.");
					System.out.println("3. 세 번째 통을 열어보자.");
					System.out.println("4. 네 번째 통을 열어보자.");
					System.out.println("5. 열어보지 말자.");
					
					num = scan.nextInt();
					
					if (num == 1)		//첫 번째 통 열어보기
					{
						System.out.println("첫 번째 통인 밥솥을 열어봤다.\n새하얀 밥이 밥솥에 담겨 있다.");
					}
					else if (num == 2)	//두 번째 통 열어보기
					{
						System.out.println("두 번째 통을 열어봤다.\n빨간 양념의 김치가 담겨 있다.");
					}
					else if (num == 3)	//세 번째 통 열어보기
					{
						System.out.println("세 번째 통을 열어봤다.\n까만 콩자반이 담겨 있다.");
					}
					else if (num == 4)	//네 번째 통 열어보기
					{
						System.out.println("마지막 통인 네 번째 통을 열어봤다.\n푸른 빛깔의 블루베리 샐러드가 담겨 있다.");
					}
					else if (num == 5)	//열어보지 않고 배식대 벗어나기
					{
						System.out.println("더 이상 살펴볼 건 없는 것 같다.");
						break;	//반복문 탈출
					}
				}
			}
			else if (num == 3)		//식수대 조사
			{
				int waterNum = 0;	//수도꼭지 돌린 순서 확인용 변수
				
				while(true)
				{
					System.out.println("식수대를 살펴봤다.\n식수대에는 색깔별로 네 개의 수도꼭지가 있다. 살펴볼까?");
					System.out.println("1. 붉은 수도꼭지를 살펴보자.");
					System.out.println("2. 푸른 수도꼭지를 살펴보자.");
					System.out.println("3. 하얀 수도꼭지를 살펴보자.");
					System.out.println("4. 까만 수도꼭지를 살펴보자.");
					System.out.println("5. 식수대 옆을 살펴보자.");
					System.out.println("6. 그만 살펴보자.");
					
					num = scan.nextInt();
					
					if (num == 1)		//붉은 수도꼭지 조사
					{
						if (waterNum == 1)		//순서가 맞을 경우 (2번째)
						{
							System.out.println("붉은 수도꼭지를 돌렸다. 물이 나온다.");
							waterNum = 2;
						}
						else if (waterNum == 0)	//첫 번째로 물을 튼 경우
						{
							System.out.println("붉은 수도꼭지를 돌렸다. 물이 나온다.");
							waterNum = 4;		//순서가 틀린 경우이므로 다음 경우에서 바로 else로 빠질 수 있도록 함.
						}
						else					//순서가 틀린 경우
						{
							System.out.println("붉은 수도꼭지를 돌리자 모든 수도꼭지에서 물이 끊겼다.");
							waterNum = 0;
						}
					}
					else if (num == 2)	//푸른 수도꼭지 조사
					{
						if (waterNum == 3)		//순서가 맞을 경우 (4번째)
						{
							//아이템(쉽배악) 가져갔는지 확인
							//가져갔을 경우
							if (player.searchItem(mapObject.getItem(0).getName()))
							{
								System.out.println("푸른 수도꼭지를 돌렸다.\n모든 물이 멈췄지만 식수대 아래에는 더 이상 아무것도 떨어지지 않았다.");
								waterNum = 0;
								continue;
							}
							//가져가지 않았을 경우
							else
							{
								//순서 올바르게 입력 -> 쉽배악 획득
								System.out.println("푸른 수도꼭지를 돌렸다.\n덜컹 하는 소리와 함께 모든 물이 멈추며 식수대 아래에서 무언가 떨어졌다.");
								waterNum = 0;
								player.saveInventory(mapObject.getItem(0));
								//쉽배악을 챙겼다.
								System.out.println("식수대 아래를 살펴보니 책 한 권이 떨어져 있다.\n" + mapObject.getItem(0).getName() + "을 챙겼다.");
							}
						}
						else if (waterNum == 0)	//첫 번째로 물을 튼 경우
						{
							System.out.println("푸른 수도꼭지를 돌렸다. 물이 나온다.");
							waterNum = 4;
						}
						else					//순서가 틀린 경우
						{
							System.out.println("푸른 수도꼭지를 돌리자 모든 수도꼭지에서 물이 끊겼다.");
							waterNum = 0;
						}
					}
					else if (num == 3)	//하얀 수도꼭지 조사
					{
						if (waterNum == 0)		//순서가 맞을 경우 (1번째)
						{
							System.out.println("하얀 수도꼭지를 돌렸다. 물이 나온다.");
							waterNum = 1;
						}
						else
						{
							System.out.println("하얀 수도꼭지를 돌리자 모든 수도꼭지에서 물이 끊겼다.");
							waterNum = 0;
						}
					}
					else if (num == 4)	//까만 수도꼭지 조사
					{
						if (waterNum == 2)		//순서가 맞을 경우 (3번째)
						{
							System.out.println("까만 수도꼭지를 돌렸다. 물이 나온다.");
							waterNum = 3;
						}
						else if (waterNum == 0)	//첫 번째로 물을 튼 경우
						{
							System.out.println("까만 수도꼭지를 돌렸다. 물이 나온다.");
							waterNum = 4;
						}
						else					//순서가 틀린 경우
						{
							System.out.println("까만 수도꼭지를 돌리자 모든 수도꼭지에서 물이 끊겼다.");
							waterNum = 0;
						}
					}
					else if (num == 5)	//식수대 옆 조사
					{
						System.out.println("식수대 옆에는 컵 소독기가 놓여 있다.");
						
						//아이템(물컵) 가져갔는지 확인
						//가져갔을 경우
						if (player.searchItem(mapObject.getItem(1).getName()))
						{
							System.out.println("더 이상 특별한 건 없어 보인다.");
							continue;
						}
						//가져가지 않았을 경우
						else
						{
							System.out.println("소독기 위에 물이 반쯤 담긴 컵이 놓여 있다. 챙길까?");
							System.out.println("1. 챙긴다.");
							System.out.println("2. 챙기지 않는다.");
							
							num = scan.nextInt();
							
							if (num == 1)		//물컵을 챙긴다
							{
								player.saveInventory(mapObject.getItem(1));
								System.out.println("혹시 필요한 일이 있을지도 모른다.\n" + mapObject.getItem(1).getName() + "을 챙겼다.");
							}
							else if (num == 2)	//물컵을 챙기지 않는다
							{
								System.out.println("굳이 챙길 필요는 없어 보인다. 그냥 두자.")	;
							}
							else				//선택지 외 번호 입력
							{
								System.out.println("챙길지 말지를 확실히 정하자.");
							}
						}
					}
				}
			}
		}
	}
	
	// 2층 영양사 사무실(방2) 이벤트 함수
	// 식당 내부에 영양사 사무실 존재
	public void playFloor2_2()
	{
		// 1. 배틀 시스템 확률
		
		// 2. 영양사 사무실 스크립트 출력
		// 회사 식당 영양사의 사무실이다. 식당 한 켠에 위치한 사무실은 단정하게 정리되어 있는 모습이다.
		// 깔끔한 책상에는 컴퓨터가 한 대 놓여 있고, 그 옆으로는 서랍장과 화분, 휴지통이 있으며 벽면에는 화이트보드가 있다.
		
		while(true)
		{
			// 2층의 두 번째(2) 방이므로 인수에 2, 2를 집어넣음
			// 방 진입 이벤트 (사물(오브젝트) 출력 및 선택 진행)
			// enterRoom의 반환값이 false면 현재 방의 이벤트를 종료하도록 함(이동)
			if (!enterRoom(2, 2))
			{
				break;
			}
			
			
			// enterRoom의 반환값이 true일 경우
			// -> 정상 진행
			if (num == 1)		//컴퓨터를 살펴볼 경우
			//2층 2번째 방 획득 아이템 : 쉽배악(0번째 obj의 0), 쉽배악(0번째 obj의 1), 일기장(1번째 obj의 0), 일기장(3번째 obj의 0)
			{
				System.out.println("책상 위에 있는 컴퓨터를 살펴봤다.\n전원을 켜자, 비밀번호가 걸려 있는 잠금화면이 보인다.");
				System.out.println("비밀번호 힌트 : '오랜만에 사람들과 일거리 구원!");
				System.out.println("비밀번호를 풀어볼까?");
				System.out.println("1. 풀어본다.");
				System.out.println("2. 풀지 않는다.");
				
				num = scan.nextInt();
				
				if (num == 1)		//비밀번호를 풀어 볼 경우
				{
					System.out.println("비밀번호를 풀어보자.\n비밀번호를 입력하시오 : ");
					int password = scan.nextInt();
					
					if (password == 5419)	//비밀번호를 맞게 입력할 경우
					{
						System.out.println("비밀번호가 풀렸다.");
						System.out.println("기본 화면의 배경화면이 보인다. 좀 더 살펴볼까?");
						System.out.println("1. 바탕화면을 살펴보자.");
						System.out.println("2. 휴지통을 살펴보자.");
						System.out.println("3. 뻐꾸기 파일을 살펴보자.");
						System.out.println("4. 그만 살펴보자.");
						
						num = scan.nextInt();
						
						if (num == 1)		//바탕화면을 살펴볼 경우
						{
							// 이미 아이템(쉽배악)을 가져갔는지 검사
							// 아이템을 이미 가져갔다면 
							if (player.searchItem(mapObject.getItem(0).getName()))
							{
								System.out.println("바탕화면에 더 이상 특별한 건 보이지 않는다.");
								continue;
							}
							//가져가지 않았다면
							else {
								System.out.println("바탕화면을 살펴보자.\n바로가기와 폴더들 사이 SBA_4.pdf라는 파일이 있다.");
								System.out.println("파일을 열어보자 '쉽게 배우는 악마어'라는 제목의 문서가 보인다.");
								player.saveInventory(mapObject.getItem(0));	//아이템(쉽배악)획득
							}
						}
						else if (num == 2)	//휴지통을 살펴볼 경우
						{
							// 이미 아이템(쉽배악)을 가져갔는지 검사
							// 아이템을 이미 가져갔다면 
							if (player.searchItem(mapObject.getItem(1).getName()))
							{
								System.out.println("휴지통에 더 이상 특별한 건 보이지 않는다.");
								continue;
							}
							//가져가지 않았다면
							else {
								System.out.println("휴지통을 살펴보자.\n휴지통을 누르자 SBA_5.pdf라는 파일이 남아 있는게 보인다.");
								System.out.println("복구하자 '쉽게 배우는 악마어'라는 제목의 문서가 열렸다.");
								player.saveInventory(mapObject.getItem(1));	//아이템(쉽배악)획득
							}
						}
						else if (num == 3)	//뻐꾸기 파일을 살펴볼 경우
						{
							System.out.println("뻐꾸기 파일을 살펴보자.\n폴더를 열자 5월_식단.xlsx, 6월_식단.xlsx... 등의 파일이 보인다.");
							System.out.println("파일을 열자 해당 월의 식단표가 뜬다. 한결같이 맛없고 부실한 반찬들뿐이다.");
						}
						else if (num == 4)	//그만 살펴볼 경우
						{
							System.out.println("컴퓨터는 이제 그만 살펴보자.");
						}
						else				//선택지 외의 번호를 입력할 경우
						{
							System.out.println("그 부분은 살펴볼 내용이 없다.");
						}
					}
					else					//잘못된 비밀번호를 입력했을 경우
					{
						System.out.println("비밀번호가 틀렸습니다.\n다시 생각해보자.");
					}
				}
				else if (num == 2)	//비밀번호를 풀지 않을 경우
				{
					System.out.println("컴퓨터는 그만 살펴보자.");
				}
				else				//선택지 외의 번호를 입력할 경우
				{
					System.out.println("비밀번호를 풀지, 풀지 않을지 정하자.");
				}
			}
			else if (num == 2)	//서랍장을 살펴볼 경우
			{
				System.out.println("서랍장에는 비밀번호가 걸려 있다.");
				System.out.println("번호를 누르는 형식으로 보인다. \n키패드는 아래와 같은 모양이다.\n12\n34\n56\n78\n");
				System.out.println("키패드 옆에 노란 포스트잇이 붙어 있다. '작은 것이 곧 시작이다.' 라는 내용이 적혀있다.");
				System.out.println("비밀번호를 풀어볼까?");
				System.out.println("1. 풀어본다.");
				System.out.println("2. 풀지 않는다.");
				
				num = scan.nextInt();
				
				if (num == 1)		//비밀번호를 풀어 볼 경우
				{
					System.out.println("비밀번호를 풀어보자.\n어떤 버튼을 입력할까? : ");
					int password = scan.nextInt();
					
					if (password == 13458)
					{
						// 이미 아이템(일기장)을 가져갔는지 검사
						// 아이템을 이미 가져갔다면 
						if (player.searchItem(mapObject.getItem(0).getName()))
						{
							System.out.println("서랍장에 더 이상 특별한 건 보이지 않는다.");
							continue;
						}
						//가져가지 않았다면
						else {
							System.out.println("서랍장이 열렸다.\n열어보자 안에 놓여 있는 작은 노트가 보인다.");
							System.out.println("일기장이다.");
							player.saveInventory(mapObject.getItem(0));	//아이템(일기장) 획득
						}
					}
					else
					{
						System.out.println("서랍장이 열리지 않는다.\n비밀번호가 틀린 것 같다. 다시 생각해보자.");
					}
				}
				else if (num == 2)	//비밀번호를 풀지 않을 경우
				{
					System.out.println("서랍장은 그만 살펴보자.");
				}
				else				//선택지 외의 번호를 입력할 경우
				{
					System.out.println("비밀번호를 풀지, 풀지 않을지 정하자.");
				}
			}
			else if (num == 3)	//화분을 살펴볼 경우
			{
				System.out.println("책상 옆의 창가에 잘 관리된 화분들이 놓여 있다.");
				System.out.println("화분의 배치가 들쑥날쑥한게 특이한 모양새다.");
				System.out.println("살펴볼까?");
				System.out.println("1. 살펴본다.");
				System.out.println("2. 살펴보지 않는다.");
				
				num = scan.nextInt();
				
				if (num == 1)		//살펴볼 경우
				{
					System.out.println("화분들을 자세히 살펴보았다.");
					System.out.println("■ \n■■\n■ \n ■");
					System.out.println("이러한 배치로 화분들이 놓여 있다.");
				}
				else if (num == 2)	//살펴보지 않을 경우
				{
					System.out.println("딱히 중요하지 않아 보인다. 화분은 그만 살펴보자.");
				}
				else				//선택지 외의 숫자를 입력할 경우
				{
					System.out.println("살펴볼지, 살펴보지 않을지 확실히 정하자.");
				}
			}
			else if (num == 4)	//휴지통을 살펴볼 경우
			{
				System.out.println("책상 옆에 놓인 작은 휴지통은 쓰레기가 반쯤 차있다. 더 자세히 확인해볼까?");
				System.out.println("1. 확인해본다.");
				System.out.println("2. 확인하지 않는다.");
				
				num = scan.nextInt();
				
				if (num == 1)		//확인할 경우
				{	
					// 이미 아이템(일기장)을 가져갔는지 검사
					// 아이템을 이미 가져갔다면 
					if (player.searchItem(mapObject.getItem(0).getName()))
					{
						System.out.println("휴지통에 더 이상 특별한 건 보이지 않는다.");
						continue;
					}
					//가져가지 않았다면
					else {
						System.out.println("휴지통을 더 뒤져보자 작은 책이 나왔다.");
						System.out.println("일기장이다.");
						player.saveInventory(mapObject.getItem(0));	//아이템(일기장) 획득
					}
				}
				else if (num == 2)	//확인하지 않을 경우
				{
					System.out.println("휴지통은 그만 살펴보자.");
				}
				else				//선택지 외의 숫자를 입력할 경우
				{
					System.out.println("확인할지, 확인하지 않을지 확실히 정하자.");
				}
			}
			else if (num == 5)	//화이트 보드를 살펴볼 경우
			{
				System.out.println("벽면에 걸린 화이트 보드에는 식단 일정을 비롯한 자잘한 내용들이 적혀 있다.");
				System.out.println("맛없어 보이는 식단을 비롯해서 특별한 내용은 없어 보이는데... 자세히 읽어볼까?");
				System.out.println("1. 읽어본다.");
				System.out.println("2. 읽지 않는다.");
				
				num = scan.nextInt();
				
				if (num == 1)		//읽어볼 경우
				{
					System.out.println("자세히 읽어보자.\n'화분 A의 식재료를 활용해 인간들에게 먹였더니, rnxh… enxhd… aptmRjdna… 의 반응을 보였다.'라는 내용이 적혀있다.");
					System.out.println("......무슨 말이지?");	
				}
				else if (num == 2)	//읽지 않을 경우
				{
					System.out.println("화이트 보드는 그만 살펴보자.");
				}
				else				//선택지 외의 숫자를 입력할 경우
				{
					System.out.println("읽을지, 말지 확실히 정하자.");
				}
			}
		}		
	}
	
	// 2층 화장실(posID: 24) 이벤트 함수
	public void playFloor2_4()
	{
		while(true)
		{
			// 2층의 네 번째(4) 방(화장실)이므로 인수에 2, 4를 집어넣음 (2층은 방이 화장실 포함 3개 뿐이지만, 통일성을 위해 4로 지정)
			// 방 진입 이벤트 (사물(오브젝트) 출력 및 선택 진행)
			// enterRoom의 반환값이 false면 현재 방의 이벤트를 종료하도록 함(이동)
			if (!enterRoom(2, 4))
			{
				break;
			}
			
			// enterRoom의 반환값이 true일 경우
			// -> 정상 진행
			
			//System.out.println("깔끔하게 청소된 화장실에서는 방향제 향이 난다.\n어떤 걸 할까?");
			//System.out.println("1. 거울을 보자");
			//System.out.println("2. 바닥을 보자");
			//System.out.println("3. 가방을 보자");
			//System.out.println("4. 나가자");
			
			//num = scan.nextInt();
			
			if (num == 1)		// 거울 보기
			{
				System.out.println("내 모습이다.");
				System.out.println("* hp: " + player.getHP());
				System.out.println("* 공격력: " + player.getAttackPower());
				System.out.println("* 방어력: " + player.getDefensivePower());
				System.out.println("* 평판도: " + player.getReputation());
			}
			
			else if (num == 2)	// 바닥 보기
			{
				System.out.println("반질반질하게 닦인 하얀 타일에 물기가 남아 있다. 넘어지지 않도록 조심하자.");			
			}
			
			else if (num == 3)	// 내 가방 보기(인벤토리, 현재 진행중인 퀘스트 확인)
			{
				System.out.println("어떤 걸 볼까?");
				System.out.println("1. 물건 위주");	// 인벤토리 확인
				System.out.println("2. 할 일 목록");	// 퀘스트 목록 확인
				
				num = scan.nextInt();
				
				if (num == 1)			// 인벤토리 확인
				{
					player.showInventory();
				}
				else if (num == 2)		// 퀘스트 확인
				{
					// 현재 플레이어가 진행 중인 퀘스트 목록 출력
					player.printQuestList();
				}
				else					// 오기입
				{
					System.out.println("그건... 잘못된 선택이다.");
				}
			}
			else if (num == 4)	//로비로 나가기
			{
				player.setPosID(20);
				break;
			}
			else				//선택지 외의 숫자를 입력한 경우 (오기입)
			{
				System.out.println("그건 화장실에서 할 수 없다.");
				
			}
		}
	}
	
	
	// * * * * * * * * * * * * * \\
	
	
	// 3층 로비 이벤트 함수
	public void playFloor3_0()
	{
		
	}
	
	// 3층 개발실1(방1) 이벤트 함수
	public void playFloor3_1()
	{
		
	}
	
	// 3층 개발실2(방2) 이벤트 함수
	public void playFloor3_2()
	{
		
	}
	
	// 3층 기술부(방3) 이벤트 함수
	public void playFloor3_3()
	{
		
	}
	
	// 3층 화장실(posID: 34) 이벤트 함수
	public void playFloor3_4()
	{
		
	}
	
	
	// * * * * * * * * * * * * * \\
	
	
	// 4층 로비 이벤트 함수
	public void playFloor4_0()
	{
		while(true)
		{
			// 1. 로비 스크립트 출력
			// 관리부와 녹화실이 있는 4층의 로비이다.
			
			// 2. 선택지 출력
			System.out.println("1. 출입기에 사원증을 찍자.");	// 저장
			System.out.println("2. 잠깐 밖에서 쉬고 오자.");	// 타이틀로 나가기 (게임 메뉴)
			System.out.println("3. 다른 곳을 둘러보자.");		// 다른 방 이동
			System.out.println("4. 엘리베이터를 타자.");		// 층 이동
			
			num = scan.nextInt();
			
			if (num == 1)			// 저장
			{
				// 현재 게임 데이터 로컬에 저장
				
				System.out.println("사원증을 성공적으로 찍었다!\n--저장되었습니다.--");
			}
			else if (num == 2)		// 저장 후 타이틀로 나가기
			{
				// 1. 현재 데이터 로컬에 저장
				
				// 2. 타이틀로 나가기
				goTitle = true;
				break;
			}
			else if (num == 3)		// 다른 방으로 이동
			{
				// 사무실, 회의실, 녹화실이 보인다. 어디로 들어갈까?
				System.out.println("1. 사무실로 들어가자.");
				System.out.println("2. 회의실로 들어가자.");
				System.out.println("3. 녹화실로 들어가자.");
				System.out.println("4. 생각이 달라졌다.");		// 다시 로비 선택지 출력
				
				num = scan.nextInt();
				
				if (num == 1)		// 사무실 들어가기
				{
					player.setPosID(41);
					break;
				}
				else if (num == 2)	// 회의실 들어가기
				{
					player.setPosID(42);
					break;
				}
				else if (num == 3)	// 녹화실 들어가기
				{
					player.setPosID(43);
					break;
				}
				else if (num == 4)	// 다른 방으로 가지 않기(로비 스크립트/선택지 다시 출력)
				{
					continue;
				}
				else
				{
					// 그 외 오기입 처리
				}
			}
			else if (num == 4)		// 다른 층으로 이동(엘리베이터 탑승)
			{
				moveFloor();
				break;
			}
			else
			{
				// 그 외 오기입 처리
			}
		}
	}
	
	// 4층 사무실(방1) 이벤트 함수
	public void playFloor4_1()
	{
		// 1. 배틀 시스템 확률
		
		// 2. 사무실 스크립트 출력
		// 관리부의 사무실이다. 퇴근을 한 지 꽤 된 듯, 싸늘한 공기가 맴돈다.
		// 책상들이 방 가운데에 있고, 구석에는 간이 탕비실이 마련되어 있다. 벽에는 게시판이 붙어 있다.
		
		while(true)
		{
			// 4층의 첫 번째(1) 방이므로 인수에 4, 1을 집어넣음
			// 방 진입 이벤트 (사물(오브젝트) 출력 및 선택 진행)
			// enterRoom의 반환값이 false면 현재 방의 이벤트를 종료하도록 함(이동)
			if (!enterRoom(4, 1))
			{
				break;
			}
			
			
			// enterRoom의 반환값이 true일 경우
			// -> 정상 진행
			
			if (num == 1)		// 책상 조사
			//4층 1번째 방 획득 아이템 : 쉽배악(1번째 obj의 0), 먹다 남은 커피(2번째 obj의 0)
			{
				while(true)
				{
					System.out.println("사무실 가운데 있는 책상쪽으로 가보자.\n각각 잘 정돈된 책상들 사이 눈에 띄는 몇 물건들이 보인다.\n어떤 걸 살펴볼까?");
					System.out.println("1. 파일철을 살펴보자.");
					System.out.println("2. 지갑을 살펴보자.");
					System.out.println("3. 핸드폰을 살펴보자.");
					System.out.println("4. 책을 살펴보자.");
					System.out.println("5. 그만 살펴보자.");
					
					num = scan.nextInt();
					
					if (num == 1)		//파일철을 살펴볼 경우
					{
						System.out.println("책상 위에 가지런히 놓여 있는 까만 파일철을 열어보았다.");
						System.out.println("□□거래처와의 거래 결산서다. ...□□ 회사는 처음 듣는 곳인데... 무슨 거래인거지?");
					}
					else if (num == 2)	//지갑을 살펴볼 경우
					{
						System.out.println("책상 위에 놓여 있던 단정한 디자인의 지갑을 열어보았다. 지갑을 회사에 두고 퇴근한건가?");
						System.out.println("지갑을 열자마자 지갑 주인으로 보이는 이의 신분증이 보인다.");
						System.out.println("----------\n마두광\n340823-XXXXXXX\n----------");
						System.out.println("......34년생이라고??");
					}
					else if (num == 3)	//핸드폰을 살펴볼 경우
					{
						System.out.println("지갑 옆에 나란히 놓여 있던 핸드폰을 살펴보았다. 핸드폰까지 회사에 두고 퇴근한건가...?");
						System.out.println("핸드폰 화면을 켜자 누군가의 얼굴이 배경으로 설정된 화면이 보인다.");
						System.out.println("지갑에서 보았던 신분증의 증명사진과 동일인으로 보인다. 같은 사람의 물건인가보다.");
						System.out.println("비밀번호가 걸려 있다. 여섯자리 비밀번호다. 풀어볼까?");
						
						System.out.println("1. 풀어본다.");
						System.out.println("2. 풀지 않는다.");
						
						num = scan.nextInt();
						
						if (num == 1)		//비밀번호를 풀어 볼 경우
						{
							System.out.println("비밀번호를 풀어보자.\n비밀번호를 입력하시오 : ");
							int password = scan.nextInt();
							
							if (password == 340823)	//비밀번호를 맞게 입력할 경우
							{
								// 아이템(쉽배악)을 가져갔는지 검사
								// 아이템을 이미 가져갔다면 
								if (player.searchItem(mapObject.getItem(0).getName()))
								{
									System.out.println("핸드폰에 더 이상 특별한 건 보이지 않는다.");
									continue;
								}
								//가져가지 않았다면
								else {
									System.out.println("비밀번호가 풀렸다! 생일이 비밀번호라니, 참 단순한 사람이네.");
									System.out.println("홈 화면에 <Easy to Learn-...> 이라는 바로가기 아이콘이 보인다.");
									System.out.println("해당 아이콘을 누르자, <쉽게 배우는 악마어> 라는 제목의 문서가 화면에 떠올랐다.")
									player.saveInventory(mapObject.getItem(0));	//아이템(쉽배악)획득
								}
							}
							else					//잘못된 비밀번호를 입력했을 경우
							{
								System.out.println("비밀번호를 잘못 입력했습니다. 이런, 다시 생각해보자.");
							}
							
						}
						else if (num == 2)	//비밀번호를 풀지 않을 경우
						{
							System.out.println("핸드폰은 그만 살펴보자.");
						}
						else				//선택지 외의 번호를 입력할 경우
						{
							System.out.println("비밀번호 입력 외에 조작할 수 있는 건 없어 보인다.");
						}
					}
					else if (num == 4)	//책을 살펴볼 경우
					{
						System.out.println("책상 한켠에 놓여 있는 두꺼운 책들을 살펴보았다.");
						System.out.println("<도전! 인간처럼 행동하는 5가지 방법>, <이렇게만 하면 당신도 프로 인간!>, <인간과 살아가기, 어렵지 않다> ... 라는 제목의 책들이다.");
						System.out.println("책 위에는 [사장님이 자주 읽으시던 책... 계속 노력하시는걸까? 대단하다.] 라는 내용이 적힌 포스트잇이 붙어 있다.");
						System.out.println("......뭐지?");
					}
					else if (num == 5)	//그만 살펴볼 경우
					{
						System.out.println("책상은 그만 살펴보자.");
					}
					else				//선택지 외 숫자 입력한 경우 (오기입)
					{
						System.out.println("그쪽은 딱히 살펴볼게 없어 보인다.");
					}
				}
			}
			else if (num == 2)		//간이탕비실 조사
			{
				while(true)
				{
					System.out.println("사무실 한켠에 있는 간이탕비실로 가보자. \n마실 수 있는 음료와 간단히 먹을 수 있는 간식류가 보인다.\n어떤 걸 살펴볼까?");
					System.out.println("1. 싱크대를 살펴보자.");
					System.out.println("2. 정수기를 살펴보자.");
					System.out.println("3. 선반을 살펴보자.");
					System.out.println("4. 서랍을 살펴보자.");
					System.out.println("5. 그만 살펴보자.");
					
					num = scan.nextInt();
					
					if (num == 1)		//싱크대를 살펴볼 경우
					{
						System.out.println("싱크대를 살펴보았다. 물을 버리고 컵을 씻는 용도로 보인다.");
						
						// 이미 아이템(커피)을 가져갔는지 검사
						// 아이템을 이미 가져갔다면 
						if (player.searchItem(mapObject.getItem(0).getName()))
						{
							System.out.println("싱크대에 더 이상 특별한 건 보이지 않는다.");
							continue;
						}
						//가져가지 않았다면
						else {
							System.out.println("싱크대 가장자리에, 누군가 먹다 남은 커피가 놓여있다.");
							System.out.println("가져갈까?");
							System.out.println("1. 가져가자.");
							System.out.println("2. 가져가지 말자.");
							
							num = scan.nextInt();
							
							if (num == 1)		//1. 가져간다. 를 선택했을 경우
							{
								// 플레이어 인벤토리에 아이템(커피) 저장
								player.saveInventory(mapObject.getItem(0));
								
								System.out.println("혹시 쓸 곳이 있을지도 모른다. 가져가자.\n" + mapObject.getItem(0).getName() + "을 챙겼다.");
							}
							else if (num == 2)	//2. 가져가지 않는다. 를 선택했을 경우
							{
								System.out.println("누가 먹다 남긴 것 같은데... 찝찝하다. 가져가지 말자.");
							}
							else				//선택지 외 번호 입력 시
							{
								System.out.println("어떻게 하자는 거지? 확실히 정하자.");
							}
						}
					}
					else if (num == 2)	//정수기를 살펴볼 경우
					{
						System.out.println("탕비실 끝쪽에 있는 작은 정수기를 살펴보았다.");
						System.out.println("거의 바닥을 드러낸 물통이 보인다.");
						System.out.println("물이 부족하네... 물통을 갈아야할텐데.");
					}
					else if (num == 3)	//선반을 살펴볼 경우
					{
						System.out.println("벽에 붙어 있는 선반을 살펴보았다.");
						System.out.println("선반에는 커피가루가 담겨있을 통 여러 개가 놓여 있다. 열어볼까?");
						System.out.println("1. 열어본다.");
						System.out.println("2. 열어보지 않는다.");
						
						num = scan.nextInt();
						
						if (num == 1)		//열어볼 경우
						{
							System.out.println("뚜껑을 열어보자 갈색의 커피가루 대신 새하얀 가루들이 가득하다.");
							System.out.println("...이게 뭐지? 설탕인가?");
							System.out.println("살짝 덜어 먹어보았지만, 설탕의 단 맛은 나지 않는다.");
							System.out.println("......뭘까?");
						}
						else if (num == 2)	//열어보지 않을 경우
						{
							System.out.println("커피통에 커피가루가 아니면 뭐가 있겠나. 굳이 열어볼 필요는 없어보인다.");
						}
						else				//오기입
						{
							System.out.println("열거나, 열지 않거나를 정하자. 저걸 부숴버릴 수는 없지 않나.");
						}
					}
					else if (num == 4)	//서랍을 살펴볼 경우
					{
						System.out.println("탕비실의 서랍을 살펴보았다.");
						System.out.println("서랍을 열자 잘 포장된 티백들이 가지런히 놓여 있다. 더 자세히 확인해볼까?");
						System.out.println("1. 확인해본다.");
						System.out.println("2. 확인하지 않는다.");
						
						num = scan.nextInt();
						
						if (num == 1)		//확인할 경우
						{
							System.out.println("티백 봉투 하나를 집어 포장을 뜯어보았다.");
							System.out.println("찻잎이 담긴 티백이 보일 줄 알았는데, 포장지 안에서 나온 건 티백이 아니라 하얀 가루가 든 팩이다.");
							System.out.println("......이게 뭐지?");
						}
						else if (num == 2)	//확인하지 않을 경우
						{
							System.out.println("티백 봉투에 티백이 있겠지...\n게다가, 우리 사무실 비품도 아닌데 마음대로 뜯어보기 꺼려진다. 그냥 넘어가자.");
						}
						else				//오기입
						{
							System.out.println("나는 확인 할 지, 확인하지 않을지를 골라야 한다.");
						}
					}
					else if (num == 5)	//그만 살펴볼 경우
					{
						System.out.println("간이탕비실은 그만 살펴보자.");
					}
					else				//선택지 외 숫자 입력 (오기입)
					{
						System.out.println("그쪽은 딱히 살펴볼게 없어 보인다.");
					}
				}
			}
			else if (num == 3)		//게시판 조사
			{
				while(true)
				{
					System.out.println("벽면에 붙어 있는 게시판으로 가보자.\n커다란 게시판에는 일정, 공지사항 등이 적혀 있다.\n어떤 걸 살펴볼까?");
					System.out.println("1. 일정표를 살펴보자.");
					System.out.println("2. 공지사항을 살펴보자.");
					System.out.println("3. 포스터를 살펴보자.");
					System.out.println("4. 그만 살펴보자.");
					
					num = scan.nextInt();
					
					if (num == 1)		//일정표를 살펴볼 경우
					{
						System.out.println("제일 넓은 면적을 차지하고 있는 달력 형태의 일정표를 살펴보았다.");
						System.out.println("\'X월 X일... 10:00 회의... \nX월 X일... 15:00 미팅...\nX월 X일... T제품 납품일...\'");
						System.out.println("어...? 방송국에서 뭘 납품한다는거지?");
					}
					else if (num == 2)	//공지사항을 살펴볼 경우
					{
						System.out.println("게시판 우측에 붙어 있는 공지사항을 살펴보았다.");
						System.out.println("\'퇴근 전 물건 정리 필수! 제대로 숨기지 않아 들킬 시 사장님과의 1:1 면담\'");
						System.out.println("...이라고 적혀 있다. 정리는 그렇다 쳐도... 틀킬 시 면담? 이건 무슨 말이지?");
					}
					else if (num == 3)	//포스터를 살펴볼 경우
					{
						System.out.println("공지사항 아래 붙어 있는 포스터들을 살펴보았다.");
						System.out.println("화재 예방 포스터, 비상 시 대피사항 등... 다양한 포스터가 붙어 있다.");
					}
					else if (num == 4)	//그만 살펴볼 경우
					{
						System.out.println("게시판은 그만 살펴보자.");
					}
					else				//오기입
					{
						System.out.println("그쪽은 딱히 살펴볼게 없어 보인다.");
					}
				}
			}
		}
	}
	
	// 4층 회의실(방2) 이벤트 함수
	public void playFloor4_2()
	{
		// 1. 배틀 시스템 확률
		
		// 2. 회의실 스크립트 출력
		// 4층의 회의실이다. 정면에 스크린과 발표용 단상이 있고, 그 앞으로 책상이 디귿자 모양으로 배치되어 있다.
		// 뒤편에는 빔 프로젝터와 컴퓨터가 있다. 회의를 진행할 때에는 어두운 상태를 유지하기에, 창문 하나 없이 막힌 분위기가 갑갑하게 느껴진다.
		
		while(true)
		{
			// 4층의 두번째(2) 방이므로 인수에 4, 2을 집어넣음
			// 방 진입 이벤트 (사물(오브젝트) 출력 및 선택 진행)
			// enterRoom의 반환값이 false면 현재 방의 이벤트를 종료하도록 함(이동)
			if (!enterRoom(4, 2))
			{
				break;
			}
			
			// enterRoom의 반환값이 true일 경우
			// -> 정상 진행
			
			if (num == 1)		// 컴퓨터 조사
			{
				while(true)
				{
					System.out.println("뒤편에 놓여 있는 컴퓨터 쪽으로 가보자. 컴퓨터 모니터에 화면보호기가 띄워져 있다.\n비밀번호는 따로 걸려 있지 않아 보인다.\n어떤 걸 살펴볼까?");
					System.out.println("1. 바탕화면을 살펴보자.");
					System.out.println("2. USB를 살펴보자.");
					System.out.println("3. 케이블을 살펴보자.");
					System.out.println("4. 나무발발이 폴더를 살펴보자.");
					System.out.println("5. 그만 살펴보자.");
					
					num = scan.nextInt();
					
					if (num == 1)		//바탕화면을 살펴볼 경우
					{
						System.out.println("컴퓨터의 바탕화면에 '거래처별_실적_현황.pptx'라는 피일이 보인다.");
						System.out.println("파일을 열어보니, 각 거래처별 판매 실적 발표 내용이 담겨있다.");
						System.out.println("PPT에는 처음 듣는 이상한 이름의 회사들이 적혀 있다.");
						System.out.println("...이런 이름의 회사가 있다고...?");
					}
					else if (num == 2)	//USB를 살펴볼 경우
					{
						System.out.println("컴퓨터에 USB가 꽂혀 있다.");
						System.out.println("USB 이름이... 이건 내가 알아볼 수 없는 글자다.");
					}
					else if (num == 3)	//케이블을 살펴볼 경우
					{
						System.out.println("컴퓨터와 빔프로젝터를 연결하는 케이블이다.");
						System.out.println("컴퓨터 화면이 스크린에 나오고 있다.");
					}
					else if (num == 4)	//나무발발이 폴더를 살펴볼 경우
					{
						System.out.println("바탕화면에 있는 유일한 폴더인 '나무발발이'폴더를 열어봤다.");
						System.out.println("안에는 '5월_수입_현황.xlsx' 라는 엑셀 파일 하나가 있다.");
						System.out.println("파일을 열어보자 낯선 제품들이 수입된 기록이 정리되어있다.");
						System.out.println("U재료, L재료... 전부 처음 보는 이름의 제품들이다. 이걸 수입해서 어디에 쓰는 걸까. 왜 수입하는 걸까.");
					}
					else if (num == 5)	//그만 살펴볼 경우
					{
						System.out.println("컴퓨터는 그만 살펴보자.");
					}
					else				//오기입
					{
						System.out.println("그건 살펴 볼 필요가 없어 보인다.");
					}
				}
			}
			else if (num == 2)		//단상 조사
			{
				while(true)
				{
					System.out.println("앞쪽의 단상 쪽으로 가보자.\n단상에는 직전에 누군가 발표 준비를 했던 흔적이 남아 있다.\n어떤 걸 살펴볼까?");
					System.out.println("1. 종이 묶음을 살펴보자.");
					System.out.println("2. 레이저포인터를 살펴보자.");
					System.out.println("3. 마이크를 살펴보자.");
					System.out.println("4. 그만 살펴보자.");
					
					num = scan.nextInt();
					
					if (num == 1)		//종이 묶음을 살펴볼 경우
					{
						System.out.println("단상 위에 있는 종이 묶음을 살펴봤다.");
						System.out.println("발표에 사용한 것 같은 대본 자료이다. 아까 보았던 PPT와 내용이 유사하다.");
						System.out.println("PPT에서 봤던 처음 듣는 낯선 회사 이름이 그대로 적혀 있다. 거래 품목도 죄다 처음 듣는 이름이다.");
					}
					else if (num == 2)		//레이저포인터를 살펴볼 경우
					{
						System.out.println("단상 위에 있는 작은 레이저포인터를 살펴봤다.");
						System.out.println("이상 없이 잘 작동한다. 발표할 때 이용한 것 같다.");
					}
					else if (num == 3)	//마이크를 살펴볼 경우
					{
						System.out.println("단상 위에 있는 마이크를 살펴봤다.");
						System.out.println("스피커와 연결되어있는 마이크다. 현재는 꺼져 있는지 작동하지 않는다.");
					}
					else if (num == 4)	//그만 살펴볼 경우
					{
						System.out.println("단상은 그만 살펴보자.");
					}
					else				//오기입
					{
						System.out.println("그 쪽은 살펴볼 게 없어 보인다.");
					}
				}
			}
			else if (num == 3)		//책상 조사
			{
				while(true)
				{
					System.out.println("중앙의 책상을 살펴보자.\n책상은 디귿자로 기다란 구조로, 깔끔하게 정리되어 있어 눈에 띄는 것은 별로 없다.\n어떤 걸 살펴볼까?");
					System.out.println("1. 종이컵을 살펴보자.");
					System.out.println("2. 볼펜을 살펴보자.");
					System.out.println("3. 포스트잇을 살펴보자.");
					System.out.println("4. 그만 살펴보자.");
					
					num = scan.nextInt();
					
					if (num == 1)		//종이컴을 살펴볼 경우
					{
						System.out.println("책상 구석에 놓여 있는 구겨진 종이컵을 살펴봤다.");
						System.out.println("무언가를 마시던 건지 종이컵 속 액체가 반쯤 남아 있다.");
						System.out.println("남은 액체에 이상한 하얀 가루가 둥둥 떠다닌다.");
						System.out.println("...이건 뭐지? 얼핏 보면 탕비실에서 보았던 하얀 가루와 비슷해 보인다.");
					}
					else if (num == 2)		//볼펜을 살펴볼 경우
					{
						System.out.println("책상 위에 놓여 있는 검정색 볼펜을 살펴봤다.");
						System.out.println("상당히 고급스러운 디자인이다. 비싸 보이는데. 이걸 두고 가다니.");
					}
					else if (num == 3)	//포스트잇을 살펴볼 경우
					{
						System.out.println("책상 아래쪽에 처박혀있던 구겨진 포스트잇을 살펴봤다.");
						System.out.println("'회의 언제 끝나지...' 같은 별 의미 없는 내용이 적혀 있다.");
					}
					else if (num == 4)	//그만 살펴볼 경우
					{
						System.out.println("책상은 그만 살펴보자.");
					}
					else				//오기입
					{
						System.out.println("그 쪽은 살펴볼 게 없어 보인다.");
					}
				}
			}
		}
	}
	
	// 4층 (방3) 이벤트 함수
	public void playFloor4_3()
	{
		
	}
	
	// 4층 화장실(posID: 44) 이벤트 함수
	public void playFloor4_4()
	{
		while(true)
		{
			// 4층의 네 번째(4) 방(화장실)이므로 인수에 4, 4를 집어넣음
			// 방 진입 이벤트 (사물(오브젝트) 출력 및 선택 진행)
			// enterRoom의 반환값이 false면 현재 방의 이벤트를 종료하도록 함(이동)
			if (!enterRoom(4, 4))
			{
				break;
			}
			
			// enterRoom의 반환값이 true일 경우
			// -> 정상 진행
			
			System.out.println("조명이 약하게 깜빡거리는 화장실은 관리가 잘 되지 않은 듯 보인다.\n어떤 걸 할까?");
			System.out.println("1. 거울을 보자");
			System.out.println("2. 바닥을 보자");
			System.out.println("3. 가방을 보자");
			System.out.println("4. 나가자");
			
			num = scan.nextInt();
			
			if (num == 1)		// 거울 보기
			{
				System.out.println("내 모습이다.");
				System.out.println("* hp: " + player.getHP());
				System.out.println("* 공격력: " + player.getAttackPower());
				System.out.println("* 방어력: " + player.getDefensivePower());
				System.out.println("* 평판도: " + player.getReputation());
			}
			
			else if (num == 2)	// 바닥 보기
			{
				System.out.println("청소가 제대로 되지 않은 것인지 하얀 찌꺼기들이 군데군데 남아 있다.");			
			}
			
			else if (num == 3)	// 내 가방 보기(인벤토리, 현재 진행중인 퀘스트 확인)
			{
				System.out.println("어떤 걸 볼까?");
				System.out.println("1. 물건 위주");	// 인벤토리 확인
				System.out.println("2. 할 일 목록");	// 퀘스트 목록 확인
				
				num = scan.nextInt();
				
				if (num == 1)			// 인벤토리 확인
				{
					player.showInventory();
				}
				else if (num == 2)		// 퀘스트 확인
				{
					// 현재 플레이어가 진행 중인 퀘스트 목록 출력
					player.printQuestList();
				}
				else					// 오기입
				{
					System.out.println("그건... 잘못된 선택이다.");
				}
			}
			else if (num == 4)	//로비로 나가기
			{
				player.setPosID(40);
				break;
			}
			else				//선택지 외의 숫자를 입력한 경우 (오기입)
			{
				System.out.println("그건 화장실에서 할 수 없다.");
				
			}
		}
	}
	
	
	// * * * * * * * * * * * * * \\
	
	
	// 5층 이벤트 함수(엔딩)
	public void playFloor5()
	{
		// 엔딩화면
		// 	=> 1. 배드엔딩: 회로선을 다 모으지 못한 경우
		//	=> 2. 노말엔딩: 회로선까지 다 모아서 사장을 성불
		// 	=> 3. 진엔딩: 평판도를 다 올려서 사장식에 오름
		// 	===> 엔딩 판별은 각 아이템별로 포인트를 부여하여, 일정 포인트를 얻으면 엔딩 보기 가능
		// 	===> 포인트는 맵에서 아이템 포인트 더해주면 됨.
		
//					if(player.getCurrentRank() == 'S'){
//						// 트루엔딩
//						// 파일에서 불러오기(읽기)
//					}
//					else if(happyEndingPoint == 3){
//						//해피엔딩
//						// 파일에서 불러오기
//					}
//					else{
//						//노말 엔딩
//						// 파일에서 불러오기
//					}
	}
}
