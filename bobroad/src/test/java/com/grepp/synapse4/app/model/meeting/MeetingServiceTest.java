package com.grepp.synapse4.app.model.meeting;

import com.grepp.synapse4.app.model.meeting.code.State;
import com.grepp.synapse4.app.model.meeting.dto.MeetingMemberDto;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.meeting.entity.MeetingMember;
import com.grepp.synapse4.app.model.meeting.repository.MeetingMemberRepository;
import com.grepp.synapse4.app.model.meeting.repository.MeetingRepository;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

    @Mock
    private MeetingMemberRepository meetingMemberRepository;

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MeetingService meetingService;

    @Test
    void findMemberByUserId() {
        // given
        Long userId = 1L;

        // 실제 객체 대신 Mockito mock 사용
        User mockUser = Mockito.mock(User.class);
        Meeting mockMeeting1 = Mockito.mock(Meeting.class);
        Meeting mockMeeting2 = Mockito.mock(Meeting.class);

        MeetingMember m1 = new MeetingMember();
        m1.setUser(mockUser);
        m1.setMeeting(mockMeeting1);
        m1.setState(State.WAIT);

        MeetingMember m2 = new MeetingMember();
        m2.setUser(mockUser);
        m2.setMeeting(mockMeeting2);
        m2.setState(State.WAIT);

        List<MeetingMember> mockInvites = List.of(m1, m2);

        when(meetingMemberRepository.findAllByUserIdAndState(userId, State.WAIT))
                .thenReturn(mockInvites);

        // when
        List<MeetingMember> result = meetingService.findInviteByUserId(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getState()).isEqualTo(State.WAIT);
        assertThat(result.get(1).getState()).isEqualTo(State.WAIT);

        System.out.println("초대된 모임 개수: " + result.size());
    }

    @Test
    void inviteUser() {
        // given
        Long meetingId = 1L;
        Long userId = 2L;

        Meeting meeting = mock(Meeting.class);
        User user = mock(User.class);

        MeetingMemberDto dto = new MeetingMemberDto(meetingId, userId);

        // mocking
        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        Meeting result = meetingService.inviteUser(dto);

        // then
        assertEquals(meeting, result); // 반환된 Meeting 이 기대값과 같은지

        System.out.println("테스트 결과");
        System.out.println("Meeting ID: " + dto.getMeetingId());
        System.out.println("User ID: " + dto.getUserId());
    }
}