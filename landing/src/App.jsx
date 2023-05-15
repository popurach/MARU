import styled from "styled-components";
import Lottie from "lottie-react";
import animationData from "./assets/lottie_building.json";
import backgroundImg from "./assets/background_layer.svg";
import logoImg from "./assets/maru_logo.png";
import gitImg from "./assets/git_logo.png";
import phoneImg from "./assets/phone2.png";
import googleImg from "./assets/google_play.png";
import qrImg from "./assets/qrcode.png";

const Sbackground = styled.div`
  background: url(${backgroundImg}), linear-gradient(118.85deg, #6039df 8.23%, #a14ab7 91.34%);
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
  width: 100vw;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
`;

const SHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 4vh 6vh;
  @media screen and (max-width: 468px) {
    padding: 4vh 4vh;
  }
`;

const SLogoImg = styled.img`
  cursor: pointer;
  height: 3vh;
`;

const SBody = styled.div`
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: start;
  flex-wrap: wrap;
  margin-top: 3vh;
  gap: 8vh;
`;

const SLeft = styled.div`
  margin-top: 4vh;
  display: flex;
  flex-direction: column;
  align-items: start;
  line-height: 7vh;
  @media screen and (max-width: 1312px) {
    align-items: center;
    line-height: 6.5vh;
    margin-top: 4vh;
  }
`;

const STitle = styled.span`
  font-size: 6vh;
  font-weight: 100;
  color: white;
  @media screen and (max-width: 700px) {
    font-size: 5.5vh;
  }
`;

const SContent = styled.span`
  margin-top: 2vh;
  font-size: 2.3vh;
  font-weight: 200;
  color: white;
  line-height: 3.5vh;
  @media screen and (max-width: 1312px) {
    width: 80vw;
    text-align: center;
  }
`;

const SQr = styled.img`
  margin-top: 4vh;
  height: 25vh;
  @media screen and (max-width: 1312px) {
    display: none;
  }
`;

const SBottom = styled.div`
  display: none;
  margin-top: 4vh;
  gap: 2vh;
  @media screen and (max-width: 1312px) {
    display: flex;
  }
`;

const SButton = styled.img`
  cursor: pointer;
  height: 6vh;
`;

const SRight = styled.div`
  position: relative;
`;

const SPhone = styled.img`
  height: 80vh;
  @media screen and (max-width: 468px) {
    height: auto;
    width: 100%;
  }
`;

const AnimatedLottie = styled(Lottie)`
  height: 30.5vh;
  position: absolute;
  top: 230px;
  left: 133px;
  @media screen and (max-width: 468px) {
    height: 25.5vh;
    top: 150px;
    left: 86px;
  }
`;

function App() {
  const marketUrl = "https://play.google.com/store/apps/details?id=com.shoebill.maru";

  return (
    <Sbackground>
      <SHeader>
        <a href={marketUrl} target="_blank" style={{ height: "3vh" }}>
          <SLogoImg src={logoImg} alt="maru_logo.png" />
        </a>
        <SLogoImg src={gitImg} alt="git_logo.png" style={{ height: "4vh" }} />
      </SHeader>
      <SBody>
        <SLeft>
          <STitle>서울에 세워보는</STitle>
          <STitle style={{ fontWeight: "bold" }}>나만의 랜드마크 !</STitle>
          <SContent>
            랜드마크에 방문해 사진으로 기록을 남기고, 랜드마크 점령 경매에 도전하세요.
            <br />
            서울을 재미있게 탐험하는 새로운 방법, 지금 바로 마루 서비스에서 만나보세요!
          </SContent>
          <SQr src={qrImg} alt="qrcode" />
          <SBottom>
            <a href={marketUrl} target="_blank" style={{ height: "6vh" }}>
              <SButton src={googleImg} alt="google" />
            </a>
          </SBottom>
        </SLeft>
        <SRight>
          <SPhone src={phoneImg} alt="phone.png" />
          <AnimatedLottie animationData={animationData} />
        </SRight>
      </SBody>
    </Sbackground>
  );
}

export default App;
