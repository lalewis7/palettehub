import { Card } from "react-bootstrap";

export default function FeedCollection(props){

    return <>
        <Card>
            <Card.Header>
                <h6>{props.name}</h6>
            </Card.Header>
        </Card>
    </>
}