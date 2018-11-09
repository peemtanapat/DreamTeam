import React from 'react'
import 'isomorphic-fetch'
import axios from '../util/axios'
import CardSubject from '../components/CardSubject'
import styled from 'styled-components'
import Router from 'next/router'
import { get } from 'http';

const CurriculumCard = styled.a`
background-color:gray;
margin:44px;
min-height:70px;
padding:11px;
size:36px;
`
export default class SubjectList extends React.Component {

    constructor() {
        super()
        this.state = {
            curriculum: [],
            subjectList: []
        }
        this.getSubjectList = this.getSubjectList.bind(this)
    }

    async componentDidMount() {
        const {data} = await axios.get('/subject-service/curriculums/')
        this.setState({ curriculum: data })
    }

    async getSubjectList(targetCurriculumId) {
        const {data} = await axios.get('subject-service/subjectlist/curriculum/'+ targetCurriculumId)
        this.setState({ subjectList: data })
    }

    redirectToVideoListPage(targetSubjectId) {

    }

    render() {

        const cardSubject = this.state.subjectList.map(subject => {
            return <CardSubject 
                    subject_id = {subject.subject_id}
                    subject_name = {subject.subject_name}
            />
        })

        return (
            <div className='container'>
                <div className='row'>
                <div className='col-6' style={{overflowY:'scroll',maxHeight:'550px'}}>
                    {this.state.curriculum.map(curriculum =>
                            <CurriculumCard onClick={() => {this.getSubjectList(curriculum.curriculumId)}} value={curriculum.curriculumId} className='card' key={curriculum.curriculumId}>
                                {curriculum.curriculumCode}
                            </CurriculumCard>
                    )}
                </div>
                <div className="col-6" style={{overflowY:'scroll',maxHeight:'550px'}}>
                    {this.state.subjectList.map(subject => (
                        <CurriculumCard key={subject.subjectId} value={subject.subjectId} className='row'>
                            {subject.subjectName}
                        </CurriculumCard>
                    ))}
                </div>
                </div>
            </div>
        )
    }
}