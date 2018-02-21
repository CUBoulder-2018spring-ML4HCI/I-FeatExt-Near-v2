# Feature extractor for the Kinect

Novel method of streaming directly from Kinect with Microsoft SDK & C++

## Setup Guide

[Tutorial - Using Kinect SDK with Visual Studio](https://youtu.be/mkSjy9mpicI)

## Video Demos

[Test - Sending OSC messages in C++](https://youtu.be/40j-6ixSpu8)

[Moving a Virtual Target with your Hand](https://youtu.be/acfYBkaEXKU)

## Disclaimer
My code is a small portion of the uploaded repository. The goal here was to integrate Skeleton Basics with Oscpacket. Most of the code is from those packages, and I do not claim credit for it. Links are below.

## Developer
Stacia Near

## Goals

Another group in this class created a feature extractor for the Kinect using Processing and SimpleOpenNI. However, this method did not work for me. So, my goal was to find a workaround for people who don't want to use Processing, or are unable to like I was.
That was just the low level goal. The higher level goal was to create a feature extractor for the Kinect in order to specify which inputs are desired, to help with my accessibility project later on. By using my feature extractor, you can easily choose a simple set of inputs to use while ignoring unnecessary data. This will greatly simplify my next project, which will use gestures of the right and left hands to control a simulated car.

## Tools

Visual Studio 2017

[Microsoft Kinect SDK 1.8](https://www.microsoft.com/en-gb/download/details.aspx?id=40278)

[Microsoft Kinect Developer's Toolkit 1.8](https://www.microsoft.com/en-gb/download/details.aspx?id=40276)

[Oscpacket 1.1.0](https://code.google.com/archive/p/oscpack)

Skeleton Basics D2D (from the Microsoft Developer Toolkit)

Wekinator

Weki Input Helper

Continuously Controlled Particle System

## Accomplishment

Streaming x,y,z data from any skeletal joint, and sending it as an OSC message that wekinator can understand. Successfully moved around a particle system using just hand motions.

## ML Choices

Neural Network - Did not provide smooth motions, probably got confused by the high number of sample data that I recorded in just one single position. My computer touchpad was far away from where I had to stand to use the Kinect, so I had to quickly get into position, record a lot of correct samples, and then try to minimize the number of incorrect samples while getting out of sensor range quickly (and the osc messages would stop once a skeleton was no longer detected.) This could be mitigated by adding some sort of "timer" plugin to the weki input helper, to only start recording, say, 5 seconds after pressing the button to take samples.
Polynomial regression (k=2) - Chosen method. Sufficiently complex, while also simple enough to handle the data quickly. Turned out very solid results in terms of moving an onscreen target with your hand.
