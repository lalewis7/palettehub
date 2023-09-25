import { Button, Col, Container, Row } from "react-bootstrap";
import { ArrowRight, ArrowRightShort, Fire, PersonHeart, Stars } from "react-bootstrap-icons";
import { Link } from "react-router-dom";
import google_logo from '../assets/google-logo.png';
import Typed from 'react-typed';
import InfiniteLooper from './../components/InfiniteLooper';
import SamplePalette from './../components/SamplePalette';
import { useState, useEffect } from 'react';

export default function LandingPage(){
    /*
    const [c1, setC1] = useState('3d8bfd');
    const [c2, setC2] = useState('8540f5');
    const [c3, setC3] = useState('8c68cd');
    const [c4, setC4] = useState('de5c9d');
    const [c5, setC5] = useState('e35d6a');

    useEffect(() => {
        const colors = [
            "3d8bfd", "0d6efd", "0a58ca", "084298", 
            "8540f5", "6610f2", "520dc2", "3d0a91", 
            "8c68cd", "6f42c1", "59359a", "432874",
            "de5c9d", "d63384", "ab296a", "801f4f",
            "e35d6a", "dc3545", "b02a37", "842029",
            "fd9843", "fd7e14", "ca6510", "984c0c",
            "ffcd39", "ffc107", "cc9a06", "997404",
            "479f76", "198754", "146c43", "0f5132",
            "4dd4ac", "20c997", "1aa179", "13795b",
            "3dd5f3", "0dcaf0", "0aa2c0", "087990"
        ]
        let i1, i2, i3, i4, i5;

        // if (!i1)
            i1 = setInterval(() => setC1(colors[Math.floor(Math.random()*colors.length)]), 5000)
        // if (!i2)
            i2 = setInterval(() => setC2(colors[Math.floor(Math.random()*colors.length)]), 8000)
        // if (!i3)
            i3 = setInterval(() => setC3(colors[Math.floor(Math.random()*colors.length)]), 7000)
        // if (!i4)
            i4 = setInterval(() => setC4(colors[Math.floor(Math.random()*colors.length)]), 6000)
        // if (!i5)
            i5 = setInterval(() => setC5(colors[Math.floor(Math.random()*colors.length)]), 9000)

        return () => {
            clearInterval(i1)
            clearInterval(i2)
            clearInterval(i3)
            clearInterval(i4)
            clearInterval(i5)
        }
    }, [])
    */

    const [c1, setC1] = useState(0);
    const [c2, setC2] = useState(0);
    const [c3, setC3] = useState(0);
    const [c4, setC4] = useState(0);
    const [c5, setC5] = useState(0);

    const colors = [
        ['e86363', 'ff640c', 'e2954c', 'e2b74c', 'e2dc4c'],
        ['204557', '8aabb2', 'dddcd8', '232e34', 'eaf2f3'],
        ['f5d5e0', 'c874b2', '7b337d', '430d4b', '210535'],
        ['dc8665', '138085', '544667', 'ce7672', 'eeb462'],
        ['8de31c', 'f6287d', 'b915cc', '210456', '0a0324']
    ]

    useEffect(() => {
        let i1, i2, i3, i4, i5;

        i1 = setInterval(() => setC1(prev => prev + 1 >= colors.length ? 0 : prev + 1), 5000)
        i2 = setInterval(() => setC2(prev => prev + 1 >= colors.length ? 0 : prev + 1), 5000)
        i3 = setInterval(() => setC3(prev => prev + 1 >= colors.length ? 0 : prev + 1), 5000)
        i4 = setInterval(() => setC4(prev => prev + 1 >= colors.length ? 0 : prev + 1), 5000)
        i5 = setInterval(() => setC5(prev => prev + 1 >= colors.length ? 0 : prev + 1), 5000)

        return () => {
            clearInterval(i1)
            clearInterval(i2)
            clearInterval(i3)
            clearInterval(i4)
            clearInterval(i5)
        }
    }, [])

    return <>
        <div className="d-flex flex-column w-100">
            <div id="landingpage-jumbo">
                <Container className="pt-5">
                    <Row>
                        <Col xs={12} lg={8} xl={8}>
                            <h1 className="landing-page-h1 display-2 mb-4">
                                Color palettes for<br />
                                <Typed
                                    strings={['designers^1000', 'artists^1000', 'color enthusiasts^1000', 'everyone!']}
                                    typeSpeed={70}
                                    backSpeed={60}
                                />
                            </h1>
                            <h5 className="landingpage-h1-text mb-4">
                                Are you a web developer styling a website? An artist in need of inspiration for your next project? 
                                Or just a color enthusiast looking for fun? 
                            </h5>
                            <h5 className="landingpage-h1-text mb-4">
                                Palette Hub allows you to inspire your creativity and put it to use by browsing, curating, and sharing color palettes 
                                of any and all aesthetics.
                            </h5>
                        </Col>
                        <Col lg={4} className="d-none d-lg-flex flex-column justify-content-center align-items-center">
                            <SamplePalette colors={[colors[c1][0], colors[c2][1], colors[c3][2], colors[c4][3], colors[c5][4]]} width="19rem" />
                        </Col>
                    </Row>
                </Container>
                <Container className="pb-5 pt-3">
                    <Row>
                        <Col>
                            <Link to="/feed/popular" className="text-reset text-decoration-none mb-3 mx-auto d-table mx-sm-0">
                                <Button variant="" className="d-flex align-items-center btn-gradient" size="lg">
                                    <Fire className="me-2" />Popular Palettes{' '}<ArrowRightShort size={26} className="ms-2" />
                                </Button>
                            </Link>
                        </Col>
                    </Row>
                </Container>
            </div>
            <div className="bg-body-tertiary">
                <Container className="py-5">
                    <Row>
                        <Col xs={3} className=" d-none d-lg-flex justify-content-center align-items-center">
                            <PersonHeart color="#3CA0FA" width="10rem" height="10rem" />
                        </Col>
                        <Col xs={12} lg={9} className="d-flex flex-column justify-content-center">
                            <h1 className="display-4 landing-page-h1 text-center text-lg-start">
                                The social media for colors
                            </h1>
                            <h5 className="text-center text-lg-start">
                            Palette Hub is home to a community of users who can post and “like” palettes, create collections, and customize their profiles.
                            </h5>
                        </Col>
                    </Row>
                </Container>
            </div>
            <Container className="py-5">
                <Row>
                    <Col className="m-auto d-flex align-items-center flex-wrap justify-content-center justify-content-lg-start">
                        <h1 className="display-4 d-inline-flex fw-bold landing-page-h1 mb-0">Hundreds of palettes</h1>
                        <Link to="/feed/new" className="text-reset text-decoration-none ms-0 ms-lg-5 mt-4 mt-lg-0">
                            <Button variant="" className="d-flex align-items-center btn-gradient-rev" size="lg">
                                <Stars className="me-2" />New Palettes{' '}<ArrowRightShort size={28} className="ms-2" />
                            </Button>
                        </Link>
                        {/* <h5 className="text-center">
                            Browse Palette Hub's countless collection of color palettes created by the community. With new palettes being added everyday.
                        </h5> */}
                    </Col>
                </Row>
            </Container>
            <div className="bg-body-secondary">
                <Container fluid className="py-5">
                    <Row>
                        <Col xs={12}>
                            <InfiniteLooper speed="20" direction="left">
                                <SamplePalette colors={['9bcc5c', 'a5ce70', '91ce42', '709f33', '597a30']} />
                                <SamplePalette colors={['025159', '3b848c', '7ab8bf', 'c4eef2', 'a67458']} />
                                <SamplePalette colors={['31b0b5', '188385', '185085', '183785', '092262']} />
                                <SamplePalette colors={['e3dda3', 'd9d083', 'bdc26f', 'a8b86a', '819e57']} />
                                <SamplePalette colors={['80a5f4', '8a80f4', 'ae80f4', 'd380f4', 'f480e7']} />
                                <SamplePalette colors={['173f5f', '20639b', '3caea3', 'f6d55c', 'ed553b']} />
                                <SamplePalette colors={['14471e', '68904d', 'c8d2d1', 'ee9b01', 'da6a00']} />
                                <SamplePalette colors={['c32483', '9c1f6d', '980e61', '6d0845', '340320']} />
                            </InfiniteLooper>
                        </Col>
                    </Row>
                    <Row className="mt-3">
                        <Col xs={12}>
                            <InfiniteLooper speed="26" direction="right">
                                <SamplePalette colors={['02315d', '00457e', '2f70af', 'b9848c', '806491']} />
                                <SamplePalette colors={['5da83a', '927718', 'e2b65d', 'c36d2c', '367c30']} />
                                <SamplePalette colors={['711c91', 'ea00d9', '0abdc6', '133e7c', '091833']} />
                                <SamplePalette colors={['ff4e50', 'fc913a', 'f9d62e', 'eae374', 'e2f4c7']} />
                                <SamplePalette colors={['256b61', '377e71', '6dafa1', 'c3d8d4', 'e1ad68']} />
                                <SamplePalette colors={['3c1518', '69140e', 'a44200', 'd58936', 'f2f3ae']} />
                                <SamplePalette colors={['5f0f40', '9a031e', 'fb8b24', 'e36414', '0f4c5c']} />
                                <SamplePalette colors={['cba8cb', 'b98dc1', 'ab65ad', '8c508c', '5b1b5b']} />
                            </InfiniteLooper>
                        </Col>
                    </Row>
                </Container>
            </div>
            <div>
                <Container className="py-5 landingpage-mw-2">
                    <Row>
                        <Col xs={12} md={3} className="d-flex justify-content-center align-items-center mb-4 mb-md-0">
                            <img src={google_logo} width={100}/>
                        </Col>
                        <Col xs={12} md={9} className="d-flex flex-column justify-content-center text-center text-md-start">
                            {/* <h3 className="landingpage-h">Becoming a user is simple and quick</h3>
                            <h5 className="text-secondary">Sign up with a Google account to post and like palettes.</h5> */}
                            <h3 className="display-5 landing-page-h1">Join the color community</h3>
                            <h5 className="">Sign up with your Google account to contribute to the growing group of artists  
                            sharing aesthetic color palettes.</h5>
                        </Col>
                    </Row>
                </Container>
            </div>
        </div>
    </>
}